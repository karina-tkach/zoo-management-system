import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {deleteData, fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import ExaminationScheduleFilter from "../components/ExaminationScheduleFilter.jsx";

export default function ExaminationsPage() {
    const [examinations, setExaminations] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();
    const [filterType, setFilterType] = useState("all");
    const [filterValue, setFilterValue] = useState("");


    useEffect(() => {
        const fetchExaminations = async () => {
            let url = `/api/examination-schedules?page=${page}&pageSize=${pageSize}`;

            if (filterType === "status" && filterValue) {
                url = `/api/examination-schedules/by-status?status=${filterValue}&page=${page}&pageSize=${pageSize}`;
            } else if (filterType !== "all") {
                return;
            }
            await fetchData({
                url: url,
                onSuccess: (data) => {
                    setExaminations(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load examinations data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchExaminations();
    }, [page, pageSize, navigate, filterType, filterValue]);

    const handleDelete = (id) => {
        deleteData({
            url: `/api/examination-schedules/${id}`,
            confirmMessage: "Are you sure you want to delete this examination schedule?",
            errorMessage: "Failed to delete examination schedule",
            onSuccess: (resData) => {
                setExaminations(examinations.filter((e) => e.id !== id));
                alert(resData.message || "Examination schedule deleted successfully");
            },
            onError: (message) => alert(message),
            navigate,
        });
    };

    const handleFilterChange = (type, value) => {
        setFilterType(type);

        if (type === "all") {
            setFilterValue("");
            setPage(1);
            return;
        }

        if (value === "") {
            setFilterValue(value);
            return;
        }

        if (value !== filterValue) {
            setFilterValue(value);
            setPage(1);
        }
    };

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title="Examination Schedules List"
            data={examinations}
            columns={[
                { name: "Animal name", value: (e) => e.animal.name },
                { name: "Animal habitat type", value: (e) => e.animal.habitatType },
                { name: "Enclosure", value: (e) => `${e.animal.enclosure.name} | ${e.animal.enclosure.location} | ${e.animal.enclosure.areaM2}`, isWrappable: true },
                { name: "Animal health status", value: (e) => e.animal.healthStatus },
                { name: "Image", value: (e) => (
                        <img
                            src={`/${e.animal.image}`}
                            alt={e.animal.name}
                            className="w-[190px] h-[120px] object-cover rounded-md border"
                            onError={(err) => (err.currentTarget.style.display = "none")}
                        />
                    ),},
                { name: "Last Checked Up At", value: (e) => e.animal.lastCheckedUpAt.slice(0, 16).replace("T", " "), isWrappable: true },
                { name: "Vet", value: (e) => `${e.veterinarian.name} | ${e.veterinarian.email}`, isWrappable: true },
                { name: "Planned time", value: (e) => e.plannedDateTime.slice(0, 16).replace("T", " "), isWrappable: true },
                { name: "Reason", value: (e) => e.reason, isWrappable: true },
                { name: "Status", value: (e) => e.status },
            ]}
            getActions={(e) => [
                <div className="flex flex-col gap-5">
                    {e.status !== 'COMPLETED' && (
                    <button
                        key="update"
                        onClick={() => navigate(`/examinations/edit/${e.id}`)}
                        className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                    >
                        Update
                    </button>)}
                    <button
                        key="delete"
                        onClick={() => handleDelete(e.id)}
                        className="bg-red-600 hover:bg-red-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                    >
                        Delete
                    </button>
                </div>,
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            addButtonPath="/examinations/add"
            addButtonText="Add Examination Schedule"
            emptyMessage="No examinations found"
            component={<ExaminationScheduleFilter
                filterType={filterType}
                filterValue={filterValue}
                onFilterChange={handleFilterChange}
            />}
        />);
}
