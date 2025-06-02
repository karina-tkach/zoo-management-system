import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function TicketsPage() {
    const [tickets, setTickets] = useState([]);
    const [page, setPage] = useState(1);
    const pageSize = 8;
    const [searchQuery, setSearchQuery] = useState("");
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();

    useEffect(() => {
        setPage(1);
        setShouldScroll(false);
    }, [searchQuery]);

    useEffect(() => {
        const fetchTickets = async () => {
            await fetchData({
                url: `/api/tickets`,
                onSuccess: (tickets) => setTickets(tickets),
                errorMessage: "Failed to load tickets data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchTickets();
    }, [navigate]);

    const filter = (data, searchQuery) => {
        return data.filter((d) =>
            (d.fullName.toLowerCase().includes(searchQuery.toLowerCase()) || d.uuid.toLowerCase().includes(searchQuery.toLowerCase()))
        );
    };

    const paginate = (data, currentPage, postsPerPage) => {
        const indexOfLastPost = currentPage * postsPerPage;
        const indexOfFirstPost = indexOfLastPost - postsPerPage;
        return {
            currentTickets: data.slice(indexOfFirstPost, indexOfLastPost),
            totalPages: Math.ceil(data.length / postsPerPage),
        };
    };

    const filteredTickets = filter(tickets, searchQuery);
    const { currentTickets, totalPages } = paginate(filteredTickets, page, pageSize);

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title="Tickets List"
            data={currentTickets}
            columns={[
                { name: "Id", value: (e) => e.id },
                { name: "UUID", value: (e) => e.uuid },
                { name: "Full name", value: (e) => e.fullName },
                { name: "Ticket type", value: (e) => e.ticketType, isWrappable: true },
                { name: "Visit type", value: (e) => e.visitType, isWrappable: true },
                { name: "Price", value: (e) => e.price },
                { name: "Visit date", value: (e) => e.visitDate },
                { name: "Purchase method", value: (e) => e.purchaseMethod },
            ]}
            getActions={(e) => [
                <button
                    key="details"
                    onClick={() => navigate(`/tickets/${e.id}`)}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                >
                    View Details
                </button>,
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            addButtonPath="/tickets/add"
            addButtonText="Add Offline Ticket"
            emptyMessage="No tickets found"
            component={
                <SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} />
            }
        />
    );
}
