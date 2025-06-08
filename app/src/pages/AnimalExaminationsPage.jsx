import React, { useEffect, useState } from "react";
import {useNavigate, useParams} from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import {ArrowLeft} from "lucide-react";

export default function AnimalExaminationsPage() {
    const { id } = useParams();
    const [examinations, setExaminations] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchExaminations = async () => {
            await fetchData({
                url: `/api/examination-schedules/by-animal/${id}?page=${page}&pageSize=${pageSize}`,
                onSuccess: (data) => {
                    setExaminations(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load examinations data by this animal",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchExaminations();
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
                <span>Examination Schedules By Animal List</span></>}
            data={examinations}
            columns={[
                { name: "Vet", value: (e) => `${e.veterinarian.name} | ${e.veterinarian.email}`, isWrappable: true },
                { name: "Planned time", value: (e) => e.plannedDateTime },
                { name: "Reason", value: (e) => e.reason },
                { name: "Status", value: (e) => e.status },
                { name: "Completed at", value: (e) => e.completedAt?.slice(0, 16).replace("T", " ") || '', isWrappable: true },
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            emptyMessage="No examinations found"
        />);
}
