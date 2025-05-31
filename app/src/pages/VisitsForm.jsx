import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

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

    const fields = [
        {
            name: 'gateId',
            label: 'Gate',
            type: 'select',
            required: true,
            options: gates.map(g => ({ value: g.id, label: `${g.name} | ${g.location}` })),
        },
        { name: 'ticketId', label: 'Ticket id', type: 'number', required: true },
        { name: 'notes', label: 'Notes', type: 'text', required: false },
    ];

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericForm
            fields={fields}
            isEdit={false}
            entityName="Visit log"
            onSubmit={onSubmit}
            cancelPath="/visits"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
        />);
}
