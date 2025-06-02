import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {useForm} from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

export default function AnimalForm() {
    const {id} = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const animalLoading = useLoading();
    const enclosuresLoading = useLoading();
    const [enclosures, setEnclosures] = useState([]);

    const {
        register,
        handleSubmit,
        reset,
        watch,
        setValue,
        formState: {errors}
    } = useForm({
        defaultValues: {
            name: '',
            species: '',
            animalGroup: '',
            habitatType: '',
            gender: '',
            birthDate: '',
            enclosure: '',
            image: null
        }
    });

    const habitatType = watch('habitatType');

    useEffect(() => {
        const loadData = async () => {
            await fetchData({
                url: `/api/animals/${id}`,
                onSuccess: (animal) =>
                    reset({
                        name: animal.name || '',
                        species: animal.species || '',
                        animalGroup: animal.animalGroup || '',
                        habitatType: animal.habitatType || '',
                        gender: animal.gender || '',
                        birthDate: animal.birthDate || '',
                        enclosure: animal.enclosure ? animal.enclosure.id.toString() : '',
                    }),
                errorMessage: "Failed to load animal data",
                navigate,
                onStart: animalLoading.start,
                onFinally: animalLoading.stop,
            });
        };

        if (isEdit) loadData();

    }, [id, isEdit, reset, navigate]);

    useEffect(() => {
        const loadData = async () => {
            if (habitatType) {
                await fetchData({
                    url: `/api/enclosures/by-environment?type=${habitatType}`,
                    onSuccess: (newEnclosures) => {
                        setEnclosures(newEnclosures);

                        const currentEnclosureId = watch('enclosure');
                        const isStillValid = newEnclosures.some(e => e.id.toString() === currentEnclosureId);

                        if (!isStillValid) {
                            setValue('enclosure', '');
                        }
                    },
                    errorMessage: "Failed to load enclosures data",
                    navigate,
                    onStart: enclosuresLoading.start,
                    onFinally: enclosuresLoading.stop,
                });
            }
        };

        loadData();

    }, [habitatType, navigate]);

    const onSubmit = async (data) => {
        const selectedEnclosure = enclosures.find(e => e.id.toString() === data.enclosure);
        if (!selectedEnclosure) {
            setServerError("Enclosure not found");
            return;
        }
        data.enclosure = selectedEnclosure;

        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/animals/${id}` : '/api/animals';

        const formData = new FormData();

        const animalPayload = {
            name: data.name,
            species: data.species,
            animalGroup: data.animalGroup,
            habitatType: data.habitatType,
            gender: data.gender,
            birthDate: data.birthDate,
            enclosure: data.enclosure,
        };

        formData.append("animal", new Blob([JSON.stringify(animalPayload)], {type: "application/json"}));

        if (data.image && data.image[0]) {
            formData.append('image', data.image[0]);
        }

        await submitData({
            url: url,
            method: method,
            data: formData,
            isJson: false,
            entityName: "Animal",
            navigate,
            successPath: "/animals",
            onValidationError: setServerError,
        });
    };

    const fields = [
        {name: "name", label: "Name", type: "text", required: true},
        {name: "species", label: "Species", type: "text", required: true},
        {
            name: 'animalGroup',
            label: 'Animal group',
            type: 'select',
            options: [
                {value: 'INVERTEBRATES', label: 'INVERTEBRATES'},
                {value: 'FISH', label: 'FISH'},
                {value: 'AMPHIBIANS', label: 'AMPHIBIANS'},
                {value: 'REPTILES', label: 'REPTILES'},
                {value: 'BIRDS', label: 'BIRDS'},
                {value: 'MAMMALS', label: 'MAMMALS'},
            ],
            required: true,
        },
        {
            name: 'habitatType',
            label: 'Habitat Type',
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
            name: 'enclosure',
            label: 'Enclosure',
            type: 'select',
            options: enclosures.map(e => ({value: e.id, label: `${e.name} | ${e.location} | ${e.areaM2}m2`})),
            required: true,
        },
        {
            name: 'gender',
            label: 'Gender',
            type: 'select',
            options: [
                {value: 'MALE', label: 'MALE'},
                {value: 'FEMALE', label: 'FEMALE'},
                {value: 'OTHER', label: 'OTHER'},
            ],
            required: true,
        },
        {name: "birthDate", label: "Date of birth", type: "date", required: true},
        {name: "image", label: "Image", type: "file", accept: "image/*", required: !isEdit},
    ];

    if (animalLoading.loading || enclosuresLoading.loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Animal"
            onSubmit={onSubmit}
            cancelPath="/animals"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
            multipart={true}
        />
    );
}
