import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {deleteData, fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function EventsPage() {
    const [events, setEvents] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
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

    const handleDelete = (id) => {
        deleteData({
            url: `/api/events/${id}`,
            confirmMessage: "Are you sure you want to delete this event?",
            errorMessage: "Failed to delete event",
            onSuccess: (resData) => {
                setEvents(events.filter((e) => e.id !== id));
                alert(resData.message || "Event deleted successfully");
            },
            onError: (message) => alert(message),
            navigate,
        });
    };

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title="Events List"
            data={events}
            columns={[
                { name: "Title", value: (e) => e.title, isWrappable: true },
                { name: "Description", value: (e) => e.description, isWrappable: true },
                { name: "Date", value: (e) => e.date },
                { name: "Start Time", value: (e) => e.startTime },
                { name: "Duration (min)", value: (e) => e.durationMinutes },
                { name: "Location", value: (e) => e.location },
                {
                    name: "Image",
                    value: (e) => (
                        <img
                            src={`/${e.image}`}
                            alt={e.title}
                            className="w-[190px] h-[120px] object-cover rounded-md border"
                            onError={(err) => (err.currentTarget.style.display = "none")}
                        />
                    ),
                },
            ]}
            getActions={(e) => [
                <button
                    key="update"
                    onClick={() => navigate(`/events/edit/${e.id}`)}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                >
                    Update
                </button>,
                <button
                    key="delete"
                    onClick={() => handleDelete(e.id)}
                    className="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                >
                    Delete
                </button>,
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            addButtonPath="/events/add"
            addButtonText="Add Event"
            emptyMessage="No events found"
        />);
}
