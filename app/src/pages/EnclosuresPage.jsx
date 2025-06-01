import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {fetchData, deleteData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function EnclosuresPage() {
    const [enclosures, setEnclosures] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(7);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchEnclosures = async () => {
            await fetchData({
                url: `/api/enclosures?page=${page}&pageSize=${pageSize}`,
                onSuccess: (enclosures) => {
                    setEnclosures(enclosures?.data);
                    setTotalPages(enclosures?.totalPages);
                },
                errorMessage: "Failed to load enclosures data",
                navigate,
                onStart: start,
                onFinally: stop,

            });
        };

        fetchEnclosures();
    }, [page, pageSize, navigate]);

    const handleDelete =  (id) => {
        deleteData({
            url: `/api/enclosures/${id}`,
            confirmMessage: "Are you sure you want to delete this enclosure?",
            errorMessage: "Failed to delete enclosure",
            onSuccess: (resData) => {
                setEnclosures(enclosures.filter((e) => e.id !== id));
                alert(resData.message || "Enclosure deleted successfully");
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
            title="Enclosures List"
            data={enclosures}
            columns={[
                { name: "Name", value: (e) => e.name },
                { name: "Location", value: (e) => e.location, isWrappable: true },
                { name: "Environment type", value: (e) => e.environmentType },
                { name: "Area (m2)", value: (e) => e.areaM2},
            ]}
            getActions={(e) => [
                <button
                    key="delails"
                    onClick={() => navigate(`/enclosures/${e.id}/animals`)}
                    className="bg-lime-600 hover:bg-lime-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                >
                    View Animals
                </button>,
                <button
                    key="update"
                    onClick={() => navigate(`/enclosures/edit/${e.id}`)}
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
            addButtonPath="/enclosures/add"
            addButtonText="Add Enclosure"
            emptyMessage="No enclosures found"
        />);
}
