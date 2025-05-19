import { useForm } from 'react-hook-form';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage() {
    const { register, handleSubmit, formState: { errors } } = useForm();
    const [serverError, setServerError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { fetchUser } = useAuth();

    const onSubmit = async (data) => {
        setLoading(true);
        setServerError('');

        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
                credentials: 'include',
            });

            if (response.status === 200) {
                await fetchUser();
                navigate('/');
            } else if (response.status === 400) {
                const resData = await response.json();
                setServerError(resData.message || 'Registration failed.');
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
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-green-50 px-4">
            <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 border border-green-200">
                <h2 className="text-3xl font-bold text-green-800 text-center mb-6">Create Account</h2>

                {serverError && (
                    <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                        {serverError}
                    </div>
                )}

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-5" noValidate>
                    <div>
                        <label htmlFor="name" className="block text-sm font-medium text-green-900 mb-1">Name</label>
                        <input
                            id="name"
                            type="text"
                            {...register('name', {required: 'Name is required'})}
                            className={`w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring ${
                                errors.name ? 'border-red-500 focus:ring-red-200' : 'border-green-300 focus:ring-green-300'
                            }`}
                            autoFocus
                        />
                        {errors.name && (
                            <p className="text-red-600 text-sm mt-1">{errors.name.message}</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-green-900 mb-1">Email</label>
                        <input
                            id="email"
                            type="email"
                            {...register('email', {required: 'Email is required'})}
                            className={`w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring ${
                                errors.email ? 'border-red-500 focus:ring-red-200' : 'border-green-300 focus:ring-green-300'
                            }`}
                        />
                        {errors.email && (
                            <p className="text-red-600 text-sm mt-1">{errors.email.message}</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="password"
                               className="block text-sm font-medium text-green-900 mb-1">Password</label>
                        <input
                            id="password"
                            type="password"
                            {...register('password', {required: 'Password is required'})}
                            className={`w-full border rounded-lg px-4 py-2 focus:outline-none focus:ring ${
                                errors.password ? 'border-red-500 focus:ring-red-200' : 'border-green-300 focus:ring-green-300'
                            }`}
                        />
                        {errors.password && (
                            <p className="text-red-600 text-sm mt-1">{errors.password.message}</p>
                        )}
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-green-800 text-white py-2 rounded-lg font-medium hover:bg-green-900 transition disabled:opacity-50"
                    >
                        {loading ? 'Registering...' : 'Register'}
                    </button>
                </form>
            </div>
        </div>
    );
}
