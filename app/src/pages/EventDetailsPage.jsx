import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { ArrowLeft } from "lucide-react";

export default function EventsView() {
    const [event, setEvent] = useState([]);
    const navigate = useNavigate();
    const { id } = useParams();
    const { loading } = useAuth();

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const response = await fetch(`/api/events/${id}`, {
                    credentials: "include",
                });

                if (response.status === 200) {
                    const data = await response.json();
                    setEvent(data || []);
                } else {
                    const resData = await response.json();
                    navigate("/error", {
                        state: {
                            message: resData.message || "Failed to load event data",
                            code: response.status,
                        },
                    });
                }
            } catch (error) {
                navigate("/error", {
                    state: {
                        message: "An unexpected error occurred",
                        code: 500,
                    },
                });
            }
        };

        fetchEvent();
    }, []);

    if (loading) {
        return (
            <div className="relative p-6 min-h-screen bg-green-50">
                <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-green-600 border-solid mx-auto mb-4" />
                        <p className="text-2xl font-semibold text-green-700">Loading...</p>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-b from-green-50 to-green-100 py-10 px-4">
            <div className="max-w-4xl mx-auto bg-white rounded-2xl shadow-lg overflow-hidden border border-green-200">
                <div className="p-4">
                    <button
                        onClick={() => navigate("/view/events")}
                        className="w-10 h-10 rounded-full bg-green-100 text-green-700 hover:bg-green-200 transition flex items-center justify-center shadow"
                        title="Back"
                    >
                        <ArrowLeft className="w-5 h-5" />
                    </button>
                </div>

                <div className="px-8 pb-10">
                    <h1 className="text-5xl text-center font-extrabold text-green-900 mb-6 font-serif leading-snug">
                        {event.title}
                    </h1>

                    {event.image && (
                        <img
                            src={`/${event.image}`}
                            alt={event.title}
                            className="w-full h-80 object-cover rounded-xl mb-8 shadow-md border"
                        />
                    )}

                    <p className="text-xl text-gray-800 mb-8 leading-relaxed">{event.description}</p>

                    <div className="space-y-4 text-green-900 text-lg font-medium">
                        <p>
                            <span className="font-extrabold">📅 Date:</span> {event.date}
                        </p>
                        <p>
                            <span className="font-extrabold">⏰ Start Time:</span> {event.startTime}
                        </p>
                        <p>
                            <span className="font-extrabold">🕒 Duration:</span> {event.durationMinutes} minutes
                        </p>
                        <p>
                            <span className="font-extrabold">📍 Location:</span> {event.location}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}
