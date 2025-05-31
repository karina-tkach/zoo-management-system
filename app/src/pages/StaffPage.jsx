import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {deleteData, fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

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
        <GenericTablePage
            title="Staff List"
            data={staff}
            columns={[
                { name: "Name", value: (e) => e.name },
                { name: "Email", value: (e) => e.email },
                { name: "Role", value: (e) => e.role },
                { name: "Hire Date", value: (e) => e.hireDate},
                { name: "Salary", value: (e) => e.salary },
                { name: "Working Days", value: (e) => e.workingDays },
                { name: "Shift", value: (e) => `${e.shiftStart} - ${e.shiftEnd}` },
            ]}
            getActions={(e) => [
                <button
                    key="update"
                    onClick={() => navigate(`/staff/edit/${e.id}`)}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                >
                    Update
                </button>,
                <button
                    key="delete"
                    onClick={() => handleDelete(e.id)}
                    className="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                >
                    Delete
                </button>,
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            addButtonPath="/staff/add"
            addButtonText="Add Staff"
            emptyMessage="No staff found"
        />);
}
