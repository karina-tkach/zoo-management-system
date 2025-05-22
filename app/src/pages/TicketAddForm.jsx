import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import {useAuth} from "../context/AuthContext.jsx";

export default function TicketAddForm() {
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const [price, setPrice] = useState(null);
    const [excursions, setExcursions] = useState([]);
    const [selectedExcursion, setSelectedExcursion] = useState(null);
    const {user, loading} = useAuth();

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
        if (!loading && !user?.roles.includes("TICKET_AGENT")) {
            navigate("/error", {
                state: {
                    message: "Access Denied: Ticket agents only",
                    code: 403,
                },
            });
        }
    }, [user, loading, navigate]);

    useEffect(() => {
        if (ticketType && visitType && !(ticketType.length === 0) && !(visitType.length === 0) ) {
            fetch(`/api/ticket-pricings/by-type?ticketType=${ticketType}&visitType=${visitType}`, {
                credentials: 'include'
            })
                .then(res => res.json())
                .then(data => setPrice(data.price))
                .catch(() => setPrice(null));
        }
    }, [ticketType, visitType]);

    useEffect(() => {
        if (visitType === 'EXCURSION') {
            fetch('/api/excursions/available', {
                credentials: 'include'
            })
                .then(res => res.json())
                .then(data => setExcursions(data))
                .catch(() => setExcursions([]));
        }
    }, [visitType]);

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
        try {
            const response = await fetch('/api/tickets', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(data),
            });
            if (response.status === 201) {
                alert('Ticket added successfully');
                navigate('/tickets');
            } else if (response.status === 400) {
                const resData = await response.json();
                setServerError(resData.message || 'Invalid input.');
            } else {
                const resData = await response.json();
                navigate('/error', {
                    state: {
                        message: resData.message || "Something went wrong",
                        code: response.status
                    }
                });
            }
        } catch (error) {
            navigate('/error', {
                state: {
                    message: "Something went wrong",
                    code: 500
                }
            });
        }
    };

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
                            <option value="">Select excursion</option>
                            {excursions.map(e => (
                                <option key={e.id} value={e.id}>{`${e.topic} | ${e.date} | ${e.startTime}`}</option>
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
