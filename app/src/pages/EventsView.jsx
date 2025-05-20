import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Pagination from "../components/Pagination";
import EventCard from "../components/EventCard";

export default function EventsView() {
    const [events, setEvents] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(9);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading } = useAuth();


    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const response = await fetch(`/api/events?page=${page}&pageSize=${pageSize}`, { credentials: "include" });

                if (response.status === 200) {
                    const data = await response.json();
                    setEvents(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                } else {
                    const resData = await response.json();
                    navigate("/error", {
                        state: {
                            message: resData.message || "Failed to load events data",
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

        fetchEvents();
    }, [page, pageSize]);

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
        <div className="bg-gradient-to-br from-green-50 to-green-100">
        <div className="max-w-7xl mx-auto p-6 scroll-target">
            <div className="text-center mb-10">
                <p className="text-green-600 text-7xl font-bold">Zoo Events</p>
                <h1 className="text-4xl font-bold text-green-800">Join Our Upcoming Adventures</h1>
                <p className="text-gray-600 text-lg mt-3">
                    From wildlife shows to educational programs, discover what's happening at the zoo.
                </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
                {events.map((event) => (
                    <EventCard key={event.id} event={event} />
                ))}
            </div>

            <Pagination
                currentPage={page}
                totalPages={totalPages}
                setCurrentPage={setPage}
                shouldScroll={shouldScroll}
                setShouldScroll={setShouldScroll}
            />
        </div>
        </div>
    );
};
