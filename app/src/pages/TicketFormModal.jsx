import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import { useForm } from "react-hook-form";
import { useAuth } from "../context/AuthContext.jsx";
import {fetchData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";
import {useLoading} from "../utils/useLoading.jsx";

export default function TicketFormModal({ visitType, excursion, onClose }) {
    const { user, loading } = useAuth();
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const isLoggedIn = user && user.username !== null;
    const [isCheckoutLoading, setIsCheckoutLoading] = useState(false);
    const {
        register,
        handleSubmit,
        setValue,
        watch,
        formState: { errors },
    } = useForm();
    const [price, setPrice] = useState(null);

    const ticketType = watch("ticketType");

    useEffect(() => {
        if (!ticketType || !visitType) return;

        const fetchPrice = async () => {
            await fetchData({
                url: `/api/ticket-pricings/by-type?ticketType=${ticketType}&visitType=${visitType}`,
                onSuccess: (data) => {
                    setPrice(data.price);
                    setValue("price", data.price);
                },
                errorMessage: "Failed to load ticket price",
                navigate,
            });
        };

        fetchPrice();
    }, [ticketType, visitType, setValue, navigate]);


    useEffect(() => {
        setValue("visitType", visitType);
        if (visitType === "EXCURSION" && excursion) {
            setValue("visitDate", excursion.date);
            setValue("excursionId", excursion.id);
        }
        setValue("email", user?.username || "");
    }, [visitType, excursion, user, setValue]);

    const onSubmit = async (data) => {
        try {
            setIsCheckoutLoading(true);
            const response = await fetch("/api/tickets/buy-ticket", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify(data),
            });

            if (response.status === 400 || response.status === 404) {
                const resData = await response.json();
                setServerError(resData.message || 'Invalid input.');
                setIsCheckoutLoading(false);
                return;
            } else if (response.status !== 200) {
                const resData = await response.json();
                navigate('/error', {
                    state: {
                        message: resData.message || "Something went wrong",
                        code: response.status
                    }
                });
            }

            const data1 = await response.json();
            const { checkoutLink } = data1;

            if (!checkoutLink || typeof checkoutLink !== "string") {
                throw new Error("Invalid or missing checkout link from server.");
            }

            window.location.href = checkoutLink;
        } catch (error) {
            navigate('/error', {
                state: {
                    message: "Something went wrong",
                    code: 500
                }
            });
        }
        finally {
            setIsCheckoutLoading(false);
        }
    };

    if (loading || isCheckoutLoading) {
        return <LoadingPage/>;
    }

    return (
        <div className={`${
            visitType === "EXCURSION"
                ? "fixed inset-0 bg-black/40 backdrop-blur-sm"
                : "my-8"
        } flex items-center justify-center z-50`}>
            <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md relative max-h-[96vh] overflow-y-auto">
                {visitType === "EXCURSION" && onClose && (
                    <button
                        onClick={onClose}
                        className="absolute top-2 right-2 text-gray-500 hover:text-red-500"
                    >
                        ✖
                    </button>
                )}
                <h2 className="text-xl font-bold mb-4">
                    {visitType === "EXCURSION" && excursion
                        ? `Book Ticket for Excursion #${excursion.id}`
                        : "Book General Admission Ticket"}
                </h2>

                {serverError && (
                    <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                        {serverError}
                    </div>
                )}

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <div>
                        <label className="text-sm">Full Name</label>
                        <input
                            className="w-full border rounded px-3 py-2"
                            {...register("fullName", { required: true })}
                        />
                        {errors.fullName && (
                            <p className="text-red-500 text-sm mt-1">
                                Full name is required
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="text-sm">Email</label>
                        <input
                            className="w-full border rounded px-3 py-2"
                            defaultValue={user?.username}
                            {...register("email", { required: true })}
                        />
                        {errors.email && (
                            <p className="text-red-500 text-sm mt-1">
                                Email is required
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="text-sm">Ticket Type</label>
                        <select
                            className="w-full border rounded px-3 py-2"
                            {...register("ticketType", { required: true })}
                        >
                            <option disabled={true} value="">
                                Select type
                            </option>
                            <option value="ADULT">ADULT</option>
                            <option value="CHILD">CHILD</option>
                            <option value="PREFERENTIAL">PREFERENTIAL</option>
                        </select>
                        {errors.ticketType && (
                            <p className="text-red-500 text-sm mt-1">
                                Ticket type is required
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="text-sm">Visit Type</label>
                        <input
                            className="w-full border rounded px-3 py-2"
                            value={visitType}
                            readOnly
                            {...register("visitType")}
                        />
                    </div>

                    {visitType === "EXCURSION" && excursion && (
                        <>
                            <div>
                                <label className="text-sm">Excursion Topic</label>
                                <input
                                    className="w-full border rounded px-3 py-2"
                                    value={excursion.topic}
                                    disabled
                                    readOnly
                                />
                            </div>
                            <input type="hidden" {...register("excursionId")} />
                        </>
                    )}

                    <div className="flex gap-4">
                        <div className="w-1/2">
                            <label className="text-sm">Visit Date</label>
                            <input
                                type="date"
                                className="w-full border rounded px-3 py-2"
                                {...register("visitDate", { required: true })}
                                defaultValue={visitType === "EXCURSION" ? excursion?.date : ""}
                                readOnly={visitType === "EXCURSION"}
                            />
                        </div>
                        <div className="w-1/2">
                            <label className="text-sm">Price</label>
                            <input
                                className="w-full border rounded px-3 py-2 bg-gray-100"
                                value={price !== null ? price : ""}
                                readOnly
                                {...register("price")}
                            />
                        </div>
                    </div>

                    <div className="text-center">
                        {isLoggedIn ? (
                            <button
                                type="submit"
                                className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                            >
                                Confirm Booking
                            </button>
                        ) : (
                            <p className="pt-2 text-center text-red-600 text-sm space-y-2 italic">Log in to book ticket</p>
                        )}
                    </div>
                </form>
            </div>
        </div>
    );
}
