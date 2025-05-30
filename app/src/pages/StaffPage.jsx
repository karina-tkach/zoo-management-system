import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Pagination from "../components/Pagination";
import {deleteData, fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";

export default function StaffPage() {
    const [staff, setStaff] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(7);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchStaff = async () => {
            await fetchData({
                url: `/api/staff?page=${page}&pageSize=${pageSize}`,
                onSuccess: (staff) => {
                    setStaff(staff?.data);
                    setTotalPages(staff?.totalPages);
                },
                errorMessage: "Failed to load staff data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchStaff();
    }, [page, pageSize, navigate]);

    const handleDelete = (id) => {
        deleteData({
            url: `/api/staff/${id}`,
            confirmMessage: "Are you sure you want to delete this staff?",
            errorMessage: "Failed to delete staff",
            onSuccess: (resData) => {
                setStaff(staff.filter((s) => s.id !== id));
                alert(resData.message || "Staff deleted successfully");
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
                <h2 className="text-2xl font-semibold text-gray-800">Staff List</h2>
                <button
                    onClick={() => navigate("/staff/add")}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md shadow"
                >
                    Add Staff
                </button>
            </div>

            <div className="overflow-x-auto border border-gray-200 rounded-md shadow-sm">
                <table className="min-w-[1000px] divide-y divide-gray-200 w-full">
                    <thead className="bg-gray-50">
                    <tr className="divide-x divide-gray-200">
                        {[
                            "Name",
                            "Email",
                            "Role",
                            "Hire Date",
                            "Salary",
                            "Working Days",
                            "Shift",
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
                    {staff.length === 0 ? (
                        <tr>
                            <td
                                colSpan={8}
                                className="text-center py-4 text-gray-500 italic"
                            >
                                No staff found
                            </td>
                        </tr>
                    ) : (
                        staff.map((s) => (
                            <tr key={s.id} className="hover:bg-gray-50 divide-x divide-gray-200">
                                <td className="px-4 py-3 whitespace-nowrap text-gray-900">
                                    {s.name}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.email}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.role}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.hireDate}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.salary}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.workingDays}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.shiftStart} - {s.shiftEnd}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap space-x-2">
                                    <button
                                        onClick={() => navigate(`/staff/edit/${s.id}`)}
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
