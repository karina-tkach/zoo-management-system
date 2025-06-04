import React, { useEffect, useState } from "react";
import {useNavigate, useParams} from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import {ArrowLeft} from "lucide-react";

export default function AnimalFeedingsPage() {
    const { id } = useParams();
    const [feedings, setFeedings] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchFeedings = async () => {
            await fetchData({
                url: `/api/feeding-schedules/by-animal/${id}?page=${page}&pageSize=${pageSize}`,
                onSuccess: (data) => {
                    setFeedings(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load feedings data by this animal",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchFeedings();
    }, [page, pageSize, navigate]);

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title={<>
            <button
                onClick={() => navigate(-1)}
                className="mb-5 w-10 h-10 rounded-full bg-emerald-200 text-green-700 hover:bg-emerald-400 transition flex items-center justify-center shadow"
                title="Back"
            >
                <ArrowLeft className="w-5 h-5"/>
            </button>
            <span>Feeding Schedules By Animal List</span></>}
            data={feedings}
            columns={[
                { name: "Caretaker", value: (e) => `${e.caretaker.name} | ${e.caretaker.email}`, isWrappable: true },
                { name: "Food type", value: (e) => e.foodType },
                { name: "Feeding time", value: (e) => e.time },
                { name: "Portion size (grams)", value: (e) => e.portionSizeGrams },
                { name: "Is done today", value: (e) => e.doneToday ? "Yes" : "No" },
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            emptyMessage="No feedings found"
        />);
}
