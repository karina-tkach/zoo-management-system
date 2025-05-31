import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function GuideExcursionsPage() {
    const [excursions, setExcursions] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchGuideExcursions = async () => {
            await fetchData({
                url: `/api/excursions/guide`,
                onSuccess: (excursions) => {
                    setExcursions(excursions?.data);
                    setTotalPages(excursions?.totalPages);
                },
                errorMessage: "Failed to load guide excursions data",
                navigate,
                onStart: start,
                onFinally: stop,

            });
        };

        fetchGuideExcursions();
    }, [page, pageSize, navigate]);

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title="My Excursions List"
            data={excursions}
            columns={[
                { name: "Topic", value: (e) => e.topic, isWrappable: true },
                { name: "Description", value: (e) => e.description, isWrappable: true },
                { name: "Date", value: (e) => e.date },
                { name: "Start time", value: (e) => e.startTime },
                { name: "Duration minutes", value: (e) => e.durationMinutes },
                { name: "Max participants", value: (e) => e.maxParticipants },
                { name: "Booked count", value: (e) => e.bookedCount },
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            emptyMessage="No excursions found"
        />);
}
