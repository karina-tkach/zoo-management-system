import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

export default function FeedingForm() {
    const { id } = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const [animals, setAnimals] = useState([]);
    const [caretakers, setCaretakers] = useState([]);
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
            caretaker: '',
            foodType: '',
            time: '',
            portionSizeGrams: ''
        }
    });

    const habitatType = watch('habitatType');

    useEffect(() => {
        const loadData = async () => {
            await fetchData({
                url: '/api/users/by-role?role=CARETAKER',
                onSuccess: (caretakers) => {
                    setCaretakers(caretakers);
                },
                errorMessage: "Failed to load caretakers data",
                navigate,
                onStart: loading.start,
                onFinally: loading.stop,
            });

            if (isEdit) {
                await fetchData({
                    url: `/api/feeding-schedules/${id}`,
                    onSuccess: (feeding) =>
                        reset({
                            habitatType: feeding.animal ? feeding.animal.habitatType : '',
                            animal: feeding.animal ? feeding.animal.id.toString() : '',
                            caretaker: feeding.caretaker ? feeding.caretaker.id.toString() : '',
                            foodType: feeding.foodType || '',
                            time: feeding.time || '',
                            portionSizeGrams: feeding.portionSizeGrams || ''
                        }),
                    errorMessage: "Failed to load feeding schedule data",
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
        const selectedCaretaker = caretakers.find(g => g.id.toString() === data.caretaker);
        if (!selectedCaretaker) {
            setServerError("Caretaker not found");
            return;
        }

        const selectedAnimal = animals.find(g => g.id.toString() === data.animal);
        if (!selectedAnimal) {
            setServerError("Animal not found");
            return;
        }

        const feedingPayload = {
            animal: selectedAnimal,
            caretaker: selectedCaretaker,
            foodType: data.foodType,
            time: data.time,
            portionSizeGrams: data.portionSizeGrams
        };

        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/feeding-schedules/${id}` : '/api/feeding-schedules';

        await submitData({
            url: url,
            method: method,
            data: feedingPayload,
            isJson: true,
            entityName: "Feeding Schedule",
            navigate,
            successPath: "/feedings",
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
            options: animals.map(g => ({ value: g.id, label: `${g.name} | ${g.species} | ${g.animalGroup}` })),
        },
        {
            name: 'caretaker',
            label: 'Caretaker',
            type: 'select',
            required: true,
            options: caretakers.map(g => ({ value: g.id, label: `${g.name} | ${g.email}` })),
        },
        { name: 'foodType', label: 'Food type', type: 'text', required: true },
        { name: 'time', label: 'Feeding Time', type: 'time', required: true },
        { name: 'portionSizeGrams', label: 'Portion Size (grams)', type: 'number', required: true },
    ];


    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Feeding Schedule"
            onSubmit={onSubmit}
            cancelPath="/feedings"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
        />);
}
