import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Pagination from "../components/Pagination";
import SearchBar from "../components/SearchBar";

export default function TicketsPage() {
    const [tickets, setTickets] = useState([]);
    const [page, setPage] = useState(1);
    const pageSize = 8;
    const [searchQuery, setSearchQuery] = useState("");
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();

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
                            "Id",
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
                                colSpan={9}
                                className="text-center py-4 text-gray-500 italic"
                            >
                                No tickets found
                            </td>
                        </tr>
                    ) : (
                        currentTickets.map((s) => (
                            <tr key={s.id} className="hover:bg-gray-50 divide-x divide-gray-200">
                                <td className="px-4 py-3 whitespace-nowrap text-gray-900">
                                    {s.id}
                                </td>
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
