import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Pagination from "../components/Pagination";

export default function VisitsPage() {
    const [visits, setVisits] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(7);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchVisits = async () => {
            try {
                const response = await fetch(`/api/visits?page=${page}&pageSize=${pageSize}`, {credentials: "include"});

                if (response.status === 200) {
                    const visits = await response.json();
                    setVisits(visits?.data);
                    setTotalPages(visits?.totalPages);
                } else {
                    const resData = await response.json();
                    navigate('/error', {
                        state: {
                            message: resData.message || 'Failed to load visits data',
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

        fetchVisits();
    }, [page, pageSize]);

    return (
        <div className="w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 scroll-target">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-800">Visits List</h2>
                <button
                    onClick={() => navigate("/visits/add")}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md shadow"
                >
                    Add Visit
                </button>
            </div>

            <div className="overflow-x-auto border border-gray-200 rounded-md shadow-sm">
                <table className="min-w-[1000px] divide-y divide-gray-200 w-full">
                    <thead className="bg-gray-50">
                    <tr className="divide-x divide-gray-200">
                        {[
                            "Gate name",
                            "Gate location",
                            "Ticket uuid",
                            "Full name",
                            "Ticket type",
                            "Visit type",
                            "Entry time",
                            "Notes"
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
                    {visits.length === 0 ? (
                        <tr>
                            <td
                                colSpan={8}
                                className="text-center py-4 text-gray-500 italic"
                            >
                                No visits found
                            </td>
                        </tr>
                    ) : (
                        visits.map((s) => (
                            <tr key={s.id} className="hover:bg-gray-50 divide-x divide-gray-200">
                                <td className="px-4 py-3 whitespace-nowrap text-gray-900">
                                    {s.gate.name}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.gate.location}
                                </td>
                                <td className="px-4 py-3 whitespace-normal break-all text-gray-700">
                                    {s.ticket.uuid}
                                </td>
                                <td className="px-4 py-3 whitespace-normal break-words text-gray-700">
                                    {s.ticket.fullName}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.ticket.ticketType}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.ticket.visitType}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.entryTime}
                                </td>
                                <td className="px-4 py-3 whitespace-normal break-words text-gray-700">
                                    {s.notes}
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
