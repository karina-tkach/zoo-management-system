import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {fetchData, submitData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import {GenericForm} from "./GenericForm.jsx";

export default function StaffForm() {
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
            email: '',
            role: '',
            hireDate: '',
            salary: '',
            workingDays: '',
            shiftStart: '',
            shiftEnd: ''
        }
    });


    useEffect(() => {
        const fetchStaff = async () => {
            await fetchData({
                url: `/api/staff/${id}`,
                onSuccess: (staff) =>
                    reset({
                        name: staff.name || '',
                        email: staff.email || '',
                        role: staff.role || '',
                        hireDate: staff.hireDate || '',
                        salary: staff.salary || '',
                        workingDays: staff.workingDays || '',
                        shiftStart: staff.shiftStart || '',
                        shiftEnd: staff.shiftEnd || ''
                    }),
                errorMessage: "Failed to load staff data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        if (isEdit) fetchStaff();
    }, [id, isEdit, reset, navigate]);

    const onSubmit = async (data) => {
        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/staff/${id}` : '/api/staff';

        await submitData({
            url: url,
            method: method,
            data,
            isJson: true,
            entityName: "Staff",
            navigate,
            successPath: "/staff",
            onValidationError: setServerError,
        });
    };

    const fields = [
        { name: 'name', label: 'Name', type: 'text', required: true },
        { name: 'email', label: 'Email', type: 'email', required: true },
        {
            name: 'role',
            label: 'Role',
            type: 'select',
            options: [
                { value: 'ADMIN', label: 'ADMIN' },
                { value: 'CARETAKER', label: 'CARETAKER' },
                { value: 'VETERINARIAN', label: 'VETERINARIAN' },
                { value: 'GUIDE', label: 'GUIDE' },
                { value: 'TICKET_AGENT', label: 'TICKET_AGENT' },
                { value: 'EVENT_MANAGER', label: 'EVENT_MANAGER' },
            ],
            required: true,
        },
        { name: 'hireDate', label: 'Hire Date', type: 'date', required: true },
        { name: 'salary', label: 'Salary', type: 'number', required: true },
        { name: 'workingDays', label: 'Working Days', type: 'text', required: true },
        { name: 'shiftStart', label: 'Shift Start', type: 'time', required: true },
        { name: 'shiftEnd', label: 'Shift End', type: 'time', required: true },
        // Password shown only when adding (not editing)
        { name: 'password', label: 'Password', type: 'password', required: true, onlyWhenAdd: true },
    ];


    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericForm
            fields={fields}
            isEdit={isEdit}
            entityName="Staff"
            onSubmit={onSubmit}
            cancelPath="/staff"
            serverError={serverError}
            register={register}
            handleSubmit={handleSubmit}
            errors={errors}
        />);
}
