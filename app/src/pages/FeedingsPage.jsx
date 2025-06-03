import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {deleteData, fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import AnimalFilter from "../components/AnimalFilter.jsx";

export default function FeedingsPage() {
    const [feedings, setFeedings] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();
    const [filterType, setFilterType] = useState("all");
    const [filterValue, setFilterValue] = useState("");


    useEffect(() => {
        const fetchFeedings = async () => {
            let url = `/api/feeding-schedules?page=${page}&pageSize=${pageSize}`;

            if (filterType === "feeding-completion" && filterValue) {
                url = `/api/feeding-schedules/by-completion?completion=${filterValue}&page=${page}&pageSize=${pageSize}`;
            } else if (filterType !== "all") {
                return;
            }
            await fetchData({
                url: url,
                onSuccess: (data) => {
                    setFeedings(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load animals data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchFeedings();
    }, [page, pageSize, navigate, filterType, filterValue]);

    const handleDelete = (id) => {
        deleteData({
            url: `/api/feeding-schedules/${id}`,
            confirmMessage: "Are you sure you want to delete this feeding schedule?",
            errorMessage: "Failed to delete feeding schedule",
            onSuccess: (resData) => {
                setFeedings(feedings.filter((e) => e.id !== id));
                alert(resData.message || "Feeding schedule deleted successfully");
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
            title="Feeding Schedules List"
            data={feedings}
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
                { name: "Last Fed Up At", value: (e) => e.animal.lastFedUpAt.slice(0, 16) },
                { name: "Caretaker", value: (e) => `${e.caretaker.name} | ${e.caretaker.email}}`, isWrappable: true },
                { name: "Food type", value: (e) => e.foodType },
                { name: "Feeding time", value: (e) => e.time },
                { name: "Portion size (grams)", value: (e) => e.portionSizeGrams },
                { name: "Is fed today", value: (e) => e.doneToday ? "Yes" : "No" },
            ]}
            getActions={(e) => [
                <button
                    key="update"
                    onClick={() => navigate(`/feedings/edit/${e.id}`)}
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
            addButtonPath="/feedings/add"
            addButtonText="Add Feeding Schedule"
            emptyMessage="No feedings found"
            /*component={<AnimalFilter
                filterType={filterType}
                filterValue={filterValue}
                onFilterChange={handleFilterChange}
            />}*/
        />);
}
