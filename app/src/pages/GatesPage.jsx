import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {deleteData, fetchData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";
import {useLoading} from "../utils/useLoading.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

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
        <GenericTablePage
            title="Gates List"
            data={gates}
            columns={[
                { name: "Gate name", value: (e) => e.name },
                { name: "Gate location", value: (e) => e.location },
            ]}
            getActions={(e) => [
                <button
                    key="update"
                    onClick={() => navigate(`/gates/edit/${e.id}`)}
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
            addButtonPath="/gates/add"
            addButtonText="Add Gate"
            emptyMessage="No gates found"
        />);
}
