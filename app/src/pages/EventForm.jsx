import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

export default function EventForm() {
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
        const fetchEvent = async () => {
            await fetchData({
                url: `/api/events/${id}`,
                onSuccess: (event) =>
                    reset({
                        title: event.title || '',
                        description: event.description || '',
                        date: event.date || '',
                        startTime: event.startTime || '',
                        durationMinutes: event.durationMinutes || '',
                        location: event.location || '',
                        image: null,
                    }),
                errorMessage: "Failed to load event data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
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

        await submitData({
            url: url,
            method: method,
            data: formData,
            isJson: false,
            entityName: "Event",
            navigate,
            successPath: "/events",
            onValidationError: setServerError,
        });
    };

    const fields = [
        { name: "title", label: "Title", type: "text", required: true },
        { name: "description", label: "Description", type: "text", required: true },
        { name: "date", label: "Date", type: "date", required: true },
        { name: "startTime", label: "Start Time", type: "time", required: true },
        {
            name: "durationMinutes",
            label: "Duration in minutes",
            type: "number",
            required: true,
        },
        { name: "location", label: "Location", type: "text", required: true },
        { name: "image", label: "Image", type: "file", accept: "image/*", required: !isEdit },
    ];

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Event"
            onSubmit={onSubmit}
            cancelPath="/events"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
            multipart={true}
        />
    );
}
