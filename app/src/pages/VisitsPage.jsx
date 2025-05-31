import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Pagination from "../components/Pagination";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function VisitsPage() {
    const [visits, setVisits] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(7);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();

    useEffect(() => {
        const fetchVisits = async () => {
            await fetchData({
                url: `/api/visits?page=${page}&pageSize=${pageSize}`,
                onSuccess: (visits) => {
                    setVisits(visits?.data);
                    setTotalPages(visits?.totalPages);
                },
                errorMessage: "Failed to load visits data",
                navigate,
                onStart: start,
                onFinally: stop
            });
        };

        fetchVisits();
    }, [page, pageSize, navigate]);

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title="Visits List"
            data={visits}
            columns={[
                { name: "Gate name", value: (e) => e.gate.name },
                { name: "Gate location", value: (e) => e.gate.location },
                { name: "Ticket uuid", value: (e) => e.ticket.uuid, isWrappable: true, breakMode: "break-all" },
                { name: "Full name", value: (e) => e.ticket.fullName, isWrappable: true },
                { name: "Ticket type", value: (e) => e.ticket.ticketType },
                { name: "Visit type", value: (e) => e.ticket.visitType },
                { name: "Entry time", value: (e) => e.entryTime },
                { name: "Notes", value: (e) => e.notes, isWrappable: true },
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            addButtonPath="/visits/add"
            addButtonText="Add Visit"
            emptyMessage="No visits found"
        />);
}
