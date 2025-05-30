import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Pagination from "../components/Pagination";
import ExcursionCard from "../components/ExcursionCard";
import TicketFormModal from "./TicketFormModal.jsx";
import {fetchData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";
import {useLoading} from "../utils/useLoading.jsx";

export default function ExcursionsView() {
    const [excursions, setExcursions] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(4);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const [selectedExcursion, setSelectedExcursion] = useState(null);
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

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <div className="bg-gradient-to-br from-green-50 py-8 px-8 to-green-100 scroll-target">
            <div className="min-h-[600px] py-8 px-4 grid gap-6 md:grid-cols-2 lg:grid-cols-4">
                {excursions.map((excursion) => (
                    <ExcursionCard key={excursion.id} excursion={excursion} onBook={() => setSelectedExcursion(excursion)}/>
                ))}
            </div>

            <Pagination
                currentPage={page}
                totalPages={totalPages}
                setCurrentPage={setPage}
                shouldScroll={shouldScroll}
                setShouldScroll={setShouldScroll}
            />

            {selectedExcursion && (
                <TicketFormModal
                    visitType={"EXCURSION"}
                    excursion={selectedExcursion}
                    onClose={() => setSelectedExcursion(null)}
                />
            )}

        </div>
    );
};
