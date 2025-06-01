import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

export default function EnclosureForm() {
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
            location: '',
            environmentType: '',
            areaM2: ''
        }
    });


    useEffect(() => {
        const fetchEnclosure = async () => {
            await fetchData({
                url: `/api/enclosures/${id}`,
                onSuccess: (enclosure) =>
                    reset({
                        name: enclosure.name || '',
                        location: enclosure.location || '',
                        environmentType: enclosure.environmentType || '',
                        areaM2: enclosure.areaM2 || '',
                    }),
                errorMessage: "Failed to load enclosure data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        if (isEdit) fetchEnclosure();

    }, [id, isEdit, reset, navigate]);

    const onSubmit = async (data) => {
        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/enclosures/${id}` : '/api/enclosures';

        await submitData({
            url: url,
            method: method,
            data,
            isJson: true,
            entityName: "Enclosure",
            navigate,
            successPath: "/enclosures",
            onValidationError: setServerError,
        });
    };

    if (loading) {
        return <LoadingPage/>;
    }

    const fields = [
        { name: 'name', label: 'Name', type: 'text', required: true },
        { name: 'location', label: 'Location', type: 'text', required: true },
        {
            name: 'environmentType',
            label: 'Environment Type',
            type: 'select',
            required: true,
            options: [
                { value: 'DESERT', label: 'DESERT' },
                { value: 'GRASSLAND', label: 'GRASSLAND' },
                { value: 'AQUATIC', label: 'AQUATIC' },
                { value: 'FOREST', label: 'FOREST' },
                { value: 'WETLAND', label: 'WETLAND' },
                { value: 'MOUNTAIN', label: 'MOUNTAIN' },
                { value: 'POLAR', label: 'POLAR' },
                { value: 'SAVANNA', label: 'SAVANNA' },
            ],
        },
        { name: 'areaM2', label: 'Area (m2)', type: 'number', required: true },
    ];


    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Enclosure"
            onSubmit={onSubmit}
            cancelPath="/enclosures"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
        />);
}
