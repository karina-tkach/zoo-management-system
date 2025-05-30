import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Pagination from "../components/Pagination";
import EventCard from "../components/EventCard";
import {fetchData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";
import {useLoading} from "../utils/useLoading.jsx";

export default function EventsView() {
    const [events, setEvents] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(9);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchEvents = async () => {
            await fetchData({
                url: `/api/events?page=${page}&pageSize=${pageSize}`,
                onSuccess: (data) => {
                    setEvents(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load events data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchEvents();
    }, [page, pageSize, navigate]);

    if (loading) {
        return <LoadingPage/>;
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
