import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";

export default function ExcursionForm() {
    const { id } = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const [guides, setGuides] = useState([]);
    const { loading, start, stop } = useLoading();

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors }
    } = useForm({
        defaultValues: {
            topic: '',
            guide: '',
            description: '',
            date: '',
            startTime: '',
            durationMinutes: '',
            maxParticipants: ''
        }
    });


    useEffect(() => {
        const loadData = async () => {
            await fetchData({
                url: '/api/users/by-role?role=GUIDE',
                onSuccess: (guides) => {
                    setGuides(guides);
                },
                errorMessage: "Failed to load guides data",
                navigate,
                onStart: start,
                onFinally: stop,
            });

            if (isEdit) {
                await fetchData({
                    url: `/api/excursions/${id}`,
                    onSuccess: (excursion) =>
                        reset({
                            topic: excursion.topic || '',
                            guide: excursion.guide ? excursion.guide.id.toString() : '',
                            description: excursion.description || '',
                            date: excursion.date || '',
                            startTime: excursion.startTime || '',
                            durationMinutes: excursion.durationMinutes || '',
                            maxParticipants: excursion.maxParticipants || ''
                        }),
                    errorMessage: "Failed to load excursion data",
                    navigate,
                    onStart: start,
                    onFinally: stop,
                });
            }
        };

        loadData();

    }, [id, isEdit, reset, navigate]);

    const onSubmit = async (data) => {
        const selectedGuide = guides.find(g => g.id.toString() === data.guide);
        if (!selectedGuide) {
            setServerError("Guide not found");
            return;
        }
        data.guide = selectedGuide;
        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/excursions/${id}` : '/api/excursions';

        await submitData({
            url: url,
            method: method,
            data,
            isJson: true,
            entityName: "Excursion",
            navigate,
            successPath: "/excursions",
            onValidationError: setServerError,
        });
    };

    if (loading) {
        return <LoadingPage/>;
    }


    return (
        <div className="max-w-xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
            <h2 className="text-2xl font-semibold mb-6 text-center">{isEdit ? 'Update Excursion' : 'Add Excursion'}</h2>
            {serverError && (
                <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                    {serverError}
                </div>
            )}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

                <div>
                    <label className="block text-sm font-medium mb-1">Topic</label>
                    <input
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('topic', {required: true})}
                    />
                    {errors.topic && <p className="text-red-500 text-sm mt-1">Topic is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Guide</label>
                    <select
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('guide', {required: true})}
                    >
                        <option value="" disabled>Select a guide</option>
                        {guides.map((guide) => (
                            <option key={guide.id} value={guide.id}>
                                {guide.name} | {guide.email}
                            </option>
                        ))}
                    </select>
                    {errors.guide && <p className="text-red-500 text-sm mt-1">Guide is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Description</label>
                    <input
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('description', {required: true})}
                    />
                    {errors.description && <p className="text-red-500 text-sm mt-1">Description is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Date</label>
                    <input type="date"
                           className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('date', {required: true})} />
                    {errors.date && <p className="text-red-500 text-sm mt-1">Date is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Start Time</label>
                    <input type="time"
                           className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('startTime', {required: true})} />
                    {errors.startTime && <p className="text-red-500 text-sm mt-1">Start time is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Duration in minutes</label>
                    <input type="number"
                           className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('durationMinutes', {required: true})} />
                    {errors.durationMinutes && <p className="text-red-500 text-sm mt-1">Duration is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Max participants</label>
                    <input type="number"
                           className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('maxParticipants', {required: true})} />
                    {errors.maxParticipants &&
                        <p className="text-red-500 text-sm mt-1">Max participants is required</p>}
                </div>

                <div className="flex justify-between items-center mt-6">
                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition"
                    >
                        {isEdit ? 'Update' : 'Add'}
                    </button>
                    <button
                        type="button"
                        onClick={() => navigate('/excursions')}
                        className="bg-gray-300 text-gray-800 px-5 py-2 rounded-lg hover:bg-gray-400 transition"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
