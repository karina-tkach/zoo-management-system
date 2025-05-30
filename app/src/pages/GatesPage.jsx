import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Pagination from "../components/Pagination";
import {deleteData, fetchData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";
import {useLoading} from "../utils/useLoading.jsx";

export default function GatesPage() {
    const [gates, setGates] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(7);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchGates = async () => {
            await fetchData({
                url: `/api/gates?page=${page}&pageSize=${pageSize}`,
                onSuccess: (gates) => {
                    setGates(gates?.data);
                    setTotalPages(gates?.totalPages);
                },
                errorMessage: "Failed to load gates data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchGates();
    }, [page, pageSize, navigate]);

    const handleDelete = (id) => {
        deleteData({
            url: `/api/gates/${id}`,
            confirmMessage: "Are you sure you want to delete this gate?",
            errorMessage: "Failed to delete gate",
            onSuccess: (resData) => {
                setGates(gates.filter((g) => g.id !== id));
                alert(resData.message || "Gate deleted successfully");
            },
            onError: (message) => alert(message),
            navigate,
        });
    };

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <div className="w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 scroll-target">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-800">Gates List</h2>
                <button
                    onClick={() => navigate("/gates/add")}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md shadow"
                >
                    Add Gate
                </button>
            </div>

            <div className="overflow-x-auto border border-gray-200 rounded-md shadow-sm">
                <table className="min-w-[1000px] divide-y divide-gray-200 w-full">
                    <thead className="bg-gray-50">
                    <tr className="divide-x divide-gray-200">
                        {[
                            "Gate name",
                            "Gate location",
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
                    {gates.length === 0 ? (
                        <tr>
                            <td
                                colSpan={3}
                                className="text-center py-4 text-gray-500 italic"
                            >
                                No gates found
                            </td>
                        </tr>
                    ) : (
                        gates.map((s) => (
                            <tr key={s.id} className="hover:bg-gray-50 divide-x divide-gray-200">
                                <td className="px-4 py-3 whitespace-nowrap text-gray-900">
                                    {s.name}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.location}
                                </td>

                                <td className="px-4 py-3 whitespace-nowrap space-x-2">
                                    <button
                                        onClick={() => navigate(`/gates/edit/${s.id}`)}
                                        className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                                    >
                                        Update
                                    </button>
                                    <button
                                        onClick={() => handleDelete(s.id)}
                                        className="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                                    >
                                        Delete
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
