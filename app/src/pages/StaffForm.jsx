import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAuth } from '../context/AuthContext';

export default function StaffForm() {
    const { id } = useParams();
    const isEdit = Boolean(id);
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const { user, loading } = useAuth();

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
        if (!loading && !user?.roles.includes("ADMIN")) {
            navigate("/error", {
                state: {
                    message: "Access Denied: Admins only",
                    code: 403,
                },
            });
        }
    }, [user, loading, navigate]);

    useEffect(() => {
        const fetchStaff = async () => {
            try {
                const response = await fetch(`/api/staff/${id}`, {credentials: "include"});

                if (response.status === 200) {
                    const staff = await response.json();
                    reset({
                        name: staff.name || '',
                        email: staff.email || '',
                        role: staff.role || '',
                        hireDate: staff.hireDate || '',
                        salary: staff.salary || '',
                        workingDays: staff.workingDays || '',
                        shiftStart: staff.shiftStart || '',
                        shiftEnd: staff.shiftEnd || ''
                    });
                } else {
                    const resData = await response.json();
                    navigate('/error', {
                        state: {
                            message: resData.message || 'Failed to load staff data',
                            code: response.status
                        }
                    });
                }
            } catch (error) {
                navigate('/error', {
                    state: {
                        message: 'An unexpected error occurred',
                        code: 500
                    }
                });
            }
        };

        if (isEdit) fetchStaff();
    }, [id, isEdit, reset, navigate]);

    const onSubmit = async (data) => {
        setServerError('');
        const method = isEdit ? 'PATCH' : 'POST';
        const url = isEdit ? `/api/staff/${id}` : '/api/staff';

        try {
            const response = await fetch(url, {
                method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
                credentials: 'include'
            });

            if (response.status === 200 || response.status === 201) {
                alert(`Staff ${isEdit ? 'updated' : 'added'} successfully`);
                navigate('/staff');
            } else if (response.status === 400) {
                const resData = await response.json();
                setServerError(resData.message || 'Invalid input.');
            } else {
                const resData = await response.json();
                navigate('/error', {
                    state: {
                        message: resData.message || "Something went wrong",
                        code: response.status
                    }
                });
            }
        } catch (error) {
            navigate('/error', {
                state: {
                    message: "Something went wrong",
                    code: 500
                }
            });
        }
    };

    if (loading)
        return (
            <div className="relative p-6 min-h-screen bg-gray-200">

                <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 border-solid mx-auto mb-4" />
                        <p className="text-xl font-semibold text-gray-700">Loading...</p>
                    </div>
                </div>
            </div>
        );

    return (
        <div className="max-w-xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6">
            <h2 className="text-2xl font-semibold mb-6 text-center">{isEdit ? 'Update Staff' : 'Add Staff'}</h2>
            {serverError && (
                <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                    {serverError}
                </div>
            )}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

                <div>
                    <label className="block text-sm font-medium mb-1">Name</label>
                    <input
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('name', { required: true })}
                    />
                    {errors.name && <p className="text-red-500 text-sm mt-1">Name is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Email</label>
                    <input
                        type="email"
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('email', { required: true })}
                    />
                    {errors.email && <p className="text-red-500 text-sm mt-1">Email is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Role</label>
                    <select
                        className="w-full border border-gray-300 rounded-lg px-3 py-2"
                        {...register('role', {required: true})}
                    >
                        <option value="" disabled={true}>Select role</option>
                        <option value="ADMIN">ADMIN</option>
                        <option value="CARETAKER">CARETAKER</option>
                        <option value="VETERINARIAN">VETERINARIAN</option>
                        <option value="GUIDE">GUIDE</option>
                        <option value="TICKET_AGENT">TICKET_AGENT</option>
                        <option value="EVENT_MANAGER">EVENT_MANAGER</option>
                        <option value="VISITOR">VISITOR</option>
                    </select>
                    {errors.role && <p className="text-red-500 text-sm mt-1">Role is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Hire Date</label>
                    <input type="date" className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('hireDate', {required: true})} />
                    {errors.hireDate && <p className="text-red-500 text-sm mt-1">Hire date is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Salary</label>
                    <input type="number" className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('salary', {required: true})} />
                    {errors.salary && <p className="text-red-500 text-sm mt-1">Salary is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Working Days</label>
                    <input className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('workingDays', {required: true})} />
                    {errors.workingDays && <p className="text-red-500 text-sm mt-1">Working days is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Shift Start</label>
                    <input type="time" className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('shiftStart', {required: true})} />
                    {errors.shiftStart && <p className="text-red-500 text-sm mt-1">Shift start is required</p>}
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">Shift End</label>
                    <input type="time" className="w-full border border-gray-300 rounded-lg px-3 py-2" {...register('shiftEnd', {required: true})} />
                    {errors.shiftEnd && <p className="text-red-500 text-sm mt-1">Shift end is required</p>}
                </div>

                {/* Password (only for Add) */}
                {!isEdit && (
                    <div>
                        <label className="block text-sm font-medium mb-1">Password</label>
                        <input
                            type="password"
                            className="w-full border border-gray-300 rounded-lg px-3 py-2"
                            {...register('password', { required: true })}
                        />
                        {errors.password && <p className="text-red-500 text-sm mt-1">Password is required</p>}
                    </div>
                )}

                <div className="flex justify-between items-center mt-6">
                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition"
                    >
                        {isEdit ? 'Update' : 'Add'}
                    </button>
                    <button
                        type="button"
                        onClick={() => navigate('/staff')}
                        className="bg-gray-300 text-gray-800 px-5 py-2 rounded-lg hover:bg-gray-400 transition"
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
