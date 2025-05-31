import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

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

    const fields = [
        { name: 'topic', label: 'Topic', type: 'text', required: true },
        {
            name: 'guide',
            label: 'Guide',
            type: 'select',
            required: true,
            options: guides.map(g => ({ value: g.id, label: `${g.name} | ${g.email}` })),
        },
        { name: 'description', label: 'Description', type: 'text', required: true },
        { name: 'date', label: 'Date', type: 'date', required: true },
        { name: 'startTime', label: 'Start Time', type: 'time', required: true },
        { name: 'durationMinutes', label: 'Duration in minutes', type: 'number', required: true },
        { name: 'maxParticipants', label: 'Max participants', type: 'number', required: true },
    ];


    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Excursion"
            onSubmit={onSubmit}
            cancelPath="/excursions"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
        />);
}
