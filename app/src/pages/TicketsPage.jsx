import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from '../context/AuthContext';
import Pagination from "../components/Pagination";
import SearchBar from "../components/SearchBar";

export default function TicketsPage() {
    const [tickets, setTickets] = useState([]);
    const [page, setPage] = useState(1);
    const pageSize = 8;
    const [searchQuery, setSearchQuery] = useState("");
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { user, loading } = useAuth();

    useEffect(() => {
        if (!loading && !user?.roles.includes("TICKET_AGENT")) {
            navigate("/error", {
                state: {
                    message: "Access Denied: Ticket agents only",
                    code: 403,
                },
            });
        }
    }, [user, loading, navigate]);

    useEffect(() => {
        setPage(1);
        setShouldScroll(false);
    }, [searchQuery]);

    useEffect(() => {
        const fetchTickets = async () => {
            try {
                const response = await fetch(`/api/tickets`, {credentials: "include"});

                if (response.status === 200) {
                    const tickets = await response.json();
                    setTickets(tickets);
                } else {
                    const resData = await response.json();
                    navigate('/error', {
                        state: {
                            message: resData.message || 'Failed to load tickets data',
                            code: response.status
                        }
                    });
                }
            } catch (error) {
                navigate('/error', {
                    state: {
                        message: 'An unexpected error occurred',
                        code: 500
                    }
                });
            }
        };

        fetchTickets();
    }, []);

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

    if (loading)
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

    return (
        <div className="w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 scroll-target">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-800">Tickets List</h2>
                <button
                    onClick={() => navigate("/tickets/add")}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md shadow"
                >
                    Add Offline Ticket
                </button>
            </div>

            <SearchBar searchQuery={searchQuery} setSearchQuery={setSearchQuery} />

            <div className="overflow-x-auto border border-gray-200 rounded-md shadow-sm mt-5">
                <table className="min-w-[1000px] divide-y divide-gray-200 w-full">
                    <thead className="bg-gray-50">
                    <tr className="divide-x divide-gray-200">
                        {[
                            "UUID",
                            "Full name",
                            "Ticket type",
                            "Visit type",
                            "Price",
                            "Visit date",
                            "Purchase method",
                            "Actions",
                        ].map((header) => (
                            <th
                                key={header}
                                className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                            >
                                {header}
                            </th>
                        ))}
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {currentTickets.length === 0 ? (
                        <tr>
                            <td
                                colSpan={8}
                                className="text-center py-4 text-gray-500 italic"
                            >
                                No tickets found
                            </td>
                        </tr>
                    ) : (
                        currentTickets.map((s) => (
                            <tr key={s.id} className="hover:bg-gray-50 divide-x divide-gray-200">
                                <td className="px-4 py-3 whitespace-nowrap text-gray-900">
                                    {s.uuid}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.fullName}
                                </td>
                                <td className="px-4 py-3 whitespace-normal break-words text-gray-700">
                                    {s.ticketType}
                                </td>
                                <td className="px-4 py-3 whitespace-normal break-words text-gray-700">
                                    {s.visitType}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.price}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.visitDate}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.purchaseMethod}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap space-x-2">
                                    <button
                                        onClick={() => navigate(`/tickets/${s.id}`)}
                                        className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                                    >
                                        View Details
                                    </button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>

            <Pagination
                currentPage={page}
                totalPages={totalPages}
                setCurrentPage={setPage}
                shouldScroll={shouldScroll}
                setShouldScroll={setShouldScroll}
            />
        </div>
    );
}
