import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {fetchData, deleteData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function ExcursionsPage() {
    const [excursions, setExcursions] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(7);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchExcursions = async () => {
            await fetchData({
                url: `/api/excursions?page=${page}&pageSize=${pageSize}`,
                onSuccess: (excursions) => {
                    setExcursions(excursions?.data);
                    setTotalPages(excursions?.totalPages);
                },
                errorMessage: "Failed to load excursions data",
                navigate,
                onStart: start,
                onFinally: stop,

            });
        };

        fetchExcursions();
    }, [page, pageSize, navigate]);

    const handleDelete =  (id) => {
        deleteData({
            url: `/api/excursions/${id}`,
            confirmMessage: "Are you sure you want to delete this excursion?",
            errorMessage: "Failed to delete excursion",
            onSuccess: (resData) => {
                setExcursions(excursions.filter((e) => e.id !== id));
                alert(resData.message || "Excursion deleted successfully");
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
            title="Excursions List"
            data={excursions}
            columns={[
                { name: "Guide name", value: (e) => e.guide.name },
                { name: "Guide email", value: (e) => e.guide.email },
                { name: "Topic", value: (e) => e.topic, isWrappable: true },
                { name: "Description", value: (e) => e.description, isWrappable: true },
                { name: "Date", value: (e) => e.date },
                { name: "Start time", value: (e) => e.startTime },
                { name: "Duration minutes", value: (e) => e.durationMinutes },
                { name: "Max participants", value: (e) => e.maxParticipants },
                { name: "Booked count", value: (e) => e.bookedCount },
            ]}
            getActions={(e) => [
                <button
                    key="update"
                    onClick={() => navigate(`/excursions/edit/${e.id}`)}
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
            addButtonPath="/excursions/add"
            addButtonText="Add Excursion"
            emptyMessage="No excursions found"
        />);
}
