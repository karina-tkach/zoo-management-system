import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";

export default function GatesForm() {
    const { id } = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const { loading, start, stop } = useLoading();

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors }
    } = useForm({
        defaultValues: {
            name: '',
            location: ''
        }
    });


    useEffect(() => {
        const fetchGate = async () => {
            await fetchData({
                url: `/api/gates/${id}`,
                onSuccess: (gate) =>
                    reset({
                        name: gate.name || '',
                        location: gate.location || ''
                    }),
                errorMessage: "Failed to load gate data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        if (isEdit) fetchGate();

    }, [id, isEdit, reset, navigate]);

    const onSubmit = async (data) => {
        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/gates/${id}` : '/api/gates';

        await submitData({
            url: url,
            method: method,
            data,
            isJson: true,
            entityName: "Gate",
            navigate,
            successPath: "/gates",
            onValidationError: setServerError,
        });
    };

    if (loading) {
        return <LoadingPage/>;
    }


    return (
        <div className="max-w-xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
            <h2 className="text-2xl font-semibold mb-6 text-center">{isEdit ? 'Update Gate' : 'Add Gate'}</h2>
            {serverError && (
                <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                    {serverError}
                </div>
            )}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

                <div>
                    <label className="block text-sm font-medium mb-1">Name</label>
                    <input
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('name', {required: true})}
                    />
                    {errors.name && <p className="text-red-500 text-sm mt-1">Name is required</p>}
                </div>


                <div>
                    <label className="block text-sm font-medium mb-1">Location</label>
                    <input
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('location', {required: true})}
                    />
                    {errors.location && <p className="text-red-500 text-sm mt-1">Location is required</p>}
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
                        onClick={() => navigate('/gates')}
                        className="bg-gray-300 text-gray-800 px-5 py-2 rounded-lg hover:bg-gray-400 transition"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
