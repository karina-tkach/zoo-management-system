import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";

export default function VisitsForm() {
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const [gates, setGates] = useState([]);
    const { loading, start, stop } = useLoading();

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        defaultValues: {
            gateId: '',
            ticketId: '',
            notes: '',
        },
    });



    useEffect(() => {
        const fetchGates = async () => {
            await fetchData({
                url: '/api/gates',
                onSuccess: (data) => {
                    setGates(data?.data)
                },
                errorMessage: "Failed to load gates data",
                navigate,
                onStart: start,
                onFinally: stop
            });
        };

        fetchGates();
    }, [navigate])

    const onSubmit = async (data) => {
        setServerError('');

        const payload = {
            gate: { id: Number(data.gateId) },
            ticket: { id: Number(data.ticketId) },
            notes: data.notes
        };

        await submitData({
            url: '/api/visits',
            method: 'POST',
            data: payload,
            isJson: true,
            entityName: "Visit log",
            navigate,
            successPath: "/visits",
            onValidationError: setServerError,
            extraAcceptedStatuses: [404],
        });
    };

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <div className="max-w-xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
            <h2 className="text-2xl font-semibold mb-6 text-center">Add Visit Log</h2>
            {serverError && (
                <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                    {serverError}
                </div>
            )}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

                <div>
                    <label className="block text-sm font-medium mb-1">Gate</label>
                    <select
                        className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('gateId', {required: true})}>
                        <option value="" disabled={true}>Select gate</option>
                        {gates.map(e => (
                            <option key={e.id}
                                    value={e.id}>{`${e.name} | ${e.location}`}</option>
                        ))}
                    </select>
                    {errors.gateId && <p className="text-red-500 text-sm mt-1">Gate is required</p>}
                </div>
                <div>
                    <label className="block text-sm font-medium mb-1">Ticket id</label>
                    <input type="number" className="w-full border border-gray-300 rounded-lg px-3 py-2 bg-gray-100"
                           {...register('ticketId', {required: true})}/>
                    {errors.ticketId && <p className="text-red-500 text-sm mt-1">Ticket id is required</p>}
                </div>
                <div>
                    <label className="block text-sm font-medium mb-1">Notes</label>
                    <input className="w-full min-h-[100px] border border-gray-300 rounded-lg px-3 py-2 bg-gray-100"
                           {...register('notes')}/>
                </div>


                <div className="flex justify-between items-center mt-6">
                    <button type="submit"
                            className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition">
                        Add Visit log
                    </button>
                    <button type="button" onClick={() => navigate('/visits')}
                            className="bg-gray-300 text-gray-800 px-5 py-2 rounded-lg hover:bg-gray-400 transition">
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
