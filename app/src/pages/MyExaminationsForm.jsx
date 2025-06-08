import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

export default function MyExaminationsForm() {
    const { id } = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const [animals, setAnimals] = useState([]);
    const loading = useLoading();
    const animalLoading = useLoading();

    const {
        register,
        handleSubmit,
        reset,
        watch,
        setValue,
        formState: { errors }
    } = useForm({
        defaultValues: {
            habitatType: '',
            animal: '',
            plannedDateTime: '',
            reason: ''
        }
    });

    const habitatType = watch('habitatType');

    useEffect(() => {
        const loadData = async () => {
            if (isEdit) {
                await fetchData({
                    url: `/api/examination-schedules/${id}`,
                    onSuccess: (examination) =>
                        reset({
                            habitatType: examination.animal ? examination.animal.habitatType : '',
                            animal: examination.animal ? examination.animal.id.toString() : '',
                            plannedDateTime: examination.plannedDateTime
                                ? examination.plannedDateTime.slice(0, 16)
                                : '',
                            reason: examination.reason || ''
                        }),
                    errorMessage: "Failed to load examination schedule data",
                    navigate,
                    onStart: loading.start,
                    onFinally: loading.stop,
                });
            }
        };

        loadData();

    }, [id, isEdit, reset, navigate]);

    useEffect(() => {
        const loadData = async () => {
            if (habitatType) {
                await fetchData({
                    url: `/api/animals/all/by-habitat?habitat=${habitatType}`,
                    onSuccess: (newAnimals) => {
                        setAnimals(newAnimals);

                        const currentAnimalId = watch('animal');
                        const isStillValid = newAnimals.some(e => e.id.toString() === currentAnimalId);

                        if (!isStillValid) {
                            setValue('animal', '');
                        }
                    },
                    errorMessage: "Failed to load animals data",
                    navigate,
                    onStart: animalLoading.start,
                    onFinally: animalLoading.stop,
                });
            }
        };

        loadData();

    }, [habitatType, navigate]);

    const onSubmit = async (data) => {
        const selectedAnimal = animals.find(g => g.id.toString() === data.animal);
        if (!selectedAnimal) {
            setServerError("Animal not found");
            return;
        }

        const examinationPayload = {
            animal: selectedAnimal,
            plannedDateTime: data.plannedDateTime,
            reason: data.reason
        };

        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/examination-schedules/${id}` : '/api/examination-schedules';

        await submitData({
            url: url,
            method: method,
            data: examinationPayload,
            isJson: true,
            entityName: "Examination Schedule",
            navigate,
            successPath: "/my/examinations",
            onValidationError: setServerError,
        });
    };

    if (loading.loading || animalLoading.loading) {
        return <LoadingPage/>;
    }

    const fields = [
        {
            name: 'habitatType',
            label: 'Animal Habitat Type',
            type: 'select',
            options: [
                {value: 'DESERT', label: 'DESERT'},
                {value: 'GRASSLAND', label: 'GRASSLAND'},
                {value: 'AQUATIC', label: 'AQUATIC'},
                {value: 'FOREST', label: 'FOREST'},
                {value: 'WETLAND', label: 'WETLAND'},
                {value: 'MOUNTAIN', label: 'MOUNTAIN'},
                {value: 'POLAR', label: 'POLAR'},
                {value: 'SAVANNA', label: 'SAVANNA'},
            ],
            required: true,
        },
        {
            name: 'animal',
            label: 'Animal',
            type: 'select',
            required: true,
            options: animals.map(g => ({ value: g.id, label: `${g.name} | ${g.habitatType} | ${g.animalGroup} | ${g.healthStatus} | ${g.lastCheckedUpAt.slice(0, 16).replace("T", " ")}` })),
        },
        { name: 'plannedDateTime', label: 'Planned Time', type: 'datetime-local', required: true },
        { name: 'reason', label: 'Reason', type: 'text', required: true },
    ];


    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Examination Schedule"
            onSubmit={onSubmit}
            cancelPath="/my/examinations"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
        />);
}
