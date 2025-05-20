import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../context/AuthContext';

export default function ExcursionForm() {
    const { id } = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const { user, loading } = useAuth();
    const [guides, setGuides] = useState([]);
    const [guidesLoaded, setGuidesLoaded] = useState(false);

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
        if (!loading && !(user?.roles.includes("ADMIN") || user?.roles.includes("EVENT_MANAGER"))) {
            navigate("/error", {
                state: {
                    message: "Access Denied: Admins or event managers only",
                    code: 403,
                },
            });
        }
    }, [user, loading, navigate]);

    useEffect(() => {
        const fetchExcursion = async () => {
            try {
                const response = await fetch(`/api/excursions/${id}`, {credentials: "include"});

                if (response.status === 200) {
                    const excursion = await response.json();
                    reset({
                        topic: excursion.topic || '',
                        guide: excursion.guide ? excursion.guide.id.toString() : '',
                        description: excursion.description || '',
                        date: excursion.date || '',
                        startTime: excursion.startTime || '',
                        durationMinutes: excursion.durationMinutes || '',
                        maxParticipants: excursion.maxParticipants || ''
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

        const fetchGuides = async () => {
            try {
                const response  = await fetch('/api/users/by-role?role=GUIDE', { credentials: 'include' });

                if (response.status === 200) {
                    const guides = await response.json();
                    setGuides(guides);
                    setGuidesLoaded(true);
                } else {
                    const resData = await response.json();
                    navigate('/error', {
                        state: {
                            message: resData.message || 'Failed to load excursion data',
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

        fetchGuides();
        if (isEdit && guidesLoaded) fetchExcursion();

    }, [id, isEdit, guidesLoaded, reset, navigate]);

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

        try {
            const response = await fetch(url, {
                method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
                credentials: 'include'
            });

            if (response.status === 200 || response.status === 201) {
                alert(`Excursion ${isEdit ? 'updated' : 'added'} successfully`);
                navigate('/excursions');
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
