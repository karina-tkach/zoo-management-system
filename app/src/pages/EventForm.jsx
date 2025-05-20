import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../context/AuthContext';

export default function EventForm() {
    const { id } = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const { user, loading } = useAuth();

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors }
    } = useForm({
        defaultValues: {
            title: '',
            description: '',
            date: '',
            startTime: '',
            durationMinutes: '',
            location: '',
            image: null
        }
    });

    useEffect(() => {
        if (!loading && !user?.roles.includes("EVENT_MANAGER")) {
            navigate("/error", {
                state: {
                    message: "Access Denied: Event managers only",
                    code: 403,
                },
            });
        }
    }, [user, loading, navigate]);

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const response = await fetch(`/api/events/${id}`, {credentials: "include"});

                if (response.status === 200) {
                    const event = await response.json();
                    reset({
                        title: event.title || '',
                        description: event.description || '',
                        date: event.date || '',
                        startTime: event.startTime || '',
                        durationMinutes: event.durationMinutes || '',
                        location: event.location || '',
                        image: null
                    });
                } else {
                    const resData = await response.json();
                    navigate('/error', {
                        state: {
                            message: resData.message || 'Failed to load staff data',
                            code: response.status
                        }
                    });
                }
            } catch (error) {
                navigate('/error', {
                    state: {
                        message: 'An unexpected error occurred',
                        code: 500
                    }
                });
            }
        };

        if (isEdit) fetchEvent();

    }, [id, isEdit, reset, navigate]);

    const onSubmit = async (data) => {
        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/events/${id}` : '/api/events';

        const formData = new FormData();

        const eventPayload = {
            title: data.title,
            description: data.description,
            date: data.date,
            startTime: data.startTime,
            durationMinutes: data.durationMinutes,
            location: data.location,
        };

        formData.append("event", new Blob([JSON.stringify(eventPayload)], { type: "application/json" }));

        if (data.image && data.image[0]) {
            formData.append('image', data.image[0]);
        }

        try {
            const response = await fetch(url, {
                method,
                body: formData,
                credentials: 'include'
            });

            if (response.status === 200 || response.status === 201) {
                alert(`Event ${isEdit ? 'updated' : 'added'} successfully`);
                navigate('/events');
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

    if (loading)
        return (
            <div className="relative p-6 min-h-screen bg-gray-200">

                <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 border-solid mx-auto mb-4" />
                        <p className="text-xl font-semibold text-gray-700">Loading...</p>
                    </div>
                </div>
            </div>
        );

    return (
        <div className="max-w-xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
            <h2 className="text-2xl font-semibold mb-6 text-center">{isEdit ? 'Update Event' : 'Add Event'}</h2>
            {serverError && (
                <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                    {serverError}
                </div>
            )}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4" encType="multipart/form-data">

                <div>
                    <label className="block text-sm font-medium mb-1">Title</label>
                    <input
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('title', {required: true})}
                    />
                    {errors.title && <p className="text-red-500 text-sm mt-1">Title is required</p>}
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
                    <label className="block text-sm font-medium mb-1">Location</label>
                    <input
                        className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('location', {required: true})} />
                    {errors.location &&
                        <p className="text-red-500 text-sm mt-1">Location is required</p>}
                </div>

                {isEdit && (<div className="mt-6 text-sm text-gray-600">
                    Leave empty to keep current image
                </div>)}

                <div>
                    <label className="block text-sm font-medium mb-1">Image</label>
                    <input
                        type="file"
                        accept="image/*"
                        {...register('image', {
                            required: !isEdit ? 'Image is required' : false
                        })}
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                    />
                    {errors.image && (
                        <p className="text-red-500 text-sm mt-1">{errors.image.message}</p>
                    )}
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
                        onClick={() => navigate('/events')}
                        className="bg-gray-300 text-gray-800 px-5 py-2 rounded-lg hover:bg-gray-400 transition"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
