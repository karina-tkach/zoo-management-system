import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import {fetchData, submitData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";
import {useLoading} from "../utils/useLoading.jsx";

export default function TicketAddForm() {
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const [price, setPrice] = useState(null);
    const [excursions, setExcursions] = useState([]);
    const [selectedExcursion, setSelectedExcursion] = useState(null);
    const excursionLoading = useLoading();

    const {
        register,
        handleSubmit,
        watch,
        setValue,
        formState: { errors },
    } = useForm({
        defaultValues: {
            fullName: '',
            ticketType: '',
            visitType: '',
            visitDate: '',
            excursionId: '',
        },
    });

    const ticketType = watch('ticketType');
    const visitType = watch('visitType');
    const excursionId = watch('excursionId');


    useEffect(() => {
        if (!ticketType || !visitType) return;

        const fetchPrice = async () => {
            await fetchData({
                url: `/api/ticket-pricings/by-type?ticketType=${ticketType}&visitType=${visitType}`,
                onSuccess: (data) => setPrice(data.price),
                errorMessage: "Failed to load ticket price",
                navigate,
            });
        };

        fetchPrice();
    }, [ticketType, visitType, navigate]);

    useEffect(() => {
        if (visitType !== 'EXCURSION') return;

        const fetchExcursions = async () => {
            await fetchData({
                url: '/api/excursions/available',
                onSuccess: (data) => setExcursions(data),
                errorMessage: "Failed to load available excursions",
                navigate,
                onStart: excursionLoading.start,
                onFinally: excursionLoading.stop,
            });
        };

        fetchExcursions();
    }, [visitType, navigate]);


    useEffect(() => {
        const selected = excursions.find(e => e.id === parseInt(excursionId));
        if (selected) {
            setSelectedExcursion(selected);
            setValue('visitDate', selected.date);
        } else {
            setSelectedExcursion(null);
        }
    }, [excursionId, excursions, setValue]);

    const onSubmit = async (data) => {
        setServerError('');
        await submitData({
            url: '/api/tickets',
            method: 'POST',
            data,
            isJson: true,
            entityName: "Ticket",
            navigate,
            successPath: "/tickets",
            onValidationError: setServerError,
        });
    };

    if (excursionLoading.loading) {
        return <LoadingPage/>;
    }

    return (
        <div className="max-w-xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
            <h2 className="text-2xl font-semibold mb-6 text-center">Add Ticket</h2>
            {serverError && (
                <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                    {serverError}
                </div>
            )}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium mb-1">Full Name</label>
                    <input className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('fullName', { required: true })} />
                    {errors.fullName && <p className="text-red-500 text-sm mt-1">Full name is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Ticket Type</label>
                    <select className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('ticketType', { required: true })}>
                        <option disabled={true} value="">Select ticket type</option>
                        <option value="ADULT">ADULT</option>
                        <option value="CHILD">CHILD</option>
                        <option value="PREFERENTIAL">PREFERENTIAL</option>
                    </select>
                    {errors.ticketType && <p className="text-red-500 text-sm mt-1">Ticket type is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Visit Type</label>
                    <select className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('visitType', { required: true })}>
                        <option disabled={true} value="">Select visit type</option>
                        <option value="GENERAL">GENERAL</option>
                        <option value="EXCURSION">EXCURSION</option>
                    </select>
                    {errors.visitType && <p className="text-red-500 text-sm mt-1">Visit type is required</p>}
                </div>

                {visitType === 'GENERAL' && (
                    <div>
                        <label className="block text-sm font-medium mb-1">Visit Date</label>
                        <input type="date" className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('visitDate', { required: true })} />
                        {errors.visitDate && <p className="text-red-500 text-sm mt-1">Visit date is required</p>}
                    </div>
                )}

                {visitType === 'EXCURSION' && (
                    <div>
                        <label className="block text-sm font-medium mb-1">Excursion</label>
                        <select className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('excursionId', { required: true })}>
                            <option value="" disabled={true}>Select excursion</option>
                            {excursions.map(e => (
                                <option key={e.id} value={e.id}>{`${e.topic} | ${e.date} | ${e.startTime} | Available: ${e.maxParticipants-e.bookedCount}`}</option>
                            ))}
                        </select>
                        {errors.excursionId && <p className="text-red-500 text-sm mt-1">Excursion is required</p>}
                    </div>

                )}

                <div>
                    <label className="block text-sm font-medium mb-1">Price</label>
                    <input className="w-full border border-gray-300 rounded-lg px-3 py-2 bg-gray-100" value={price !== null ? price : ''} disabled readOnly />
                </div>

                <div className="flex justify-between items-center mt-6">
                    <button type="submit" className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition">
                        Add Ticket
                    </button>
                    <button type="button" onClick={() => navigate('/tickets')} className="bg-gray-300 text-gray-800 px-5 py-2 rounded-lg hover:bg-gray-400 transition">
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
