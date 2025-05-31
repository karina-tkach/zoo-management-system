import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

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

    const fields = [
        { name: 'name', label: 'Name', type: 'text', required: true },
        { name: 'location', label: 'Location', type: 'text', required: true },
    ];

    if (loading) {
        return <LoadingPage/>;
    }


    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Gate"
            onSubmit={onSubmit}
            cancelPath="/gates"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
        />);
}
