import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useAuth } from "../context/AuthContext";

export default function TicketFormModal({ visitType, excursion, onClose }) {
    const { user, loading } = useAuth();
    const isLoggedIn = user && user.username !== null;
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
        if (ticketType && visitType) {
            fetch(`/api/ticket-pricings/by-type?ticketType=${ticketType}&visitType=${visitType}`, {
                credentials: "include",
            })
                .then(res => res.json())
                .then(data => {
                    setPrice(data.price);
                    setValue("price", data.price);
                })
                .catch(() => setPrice(null));
        }
    }, [ticketType, visitType, setValue]);

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
            const res = await fetch("/api/tickets", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify(data),
            });

            if (res.status === 201) {
                alert("Ticket created successfully");
                onClose?.();
            } else {
                const err = await res.json();
                alert(err.message || "Failed to create ticket");
            }
        } catch (err) {
            alert("Something went wrong");
        }
    };

    if (loading) {
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
                            <option disabled value="">
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
