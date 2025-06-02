import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {deleteData, fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import AnimalFilter from "../components/AnimalFilter.jsx";

export default function AnimalsPage() {
    const [animals, setAnimals] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();
    const [filterType, setFilterType] = useState("all");
    const [filterValue, setFilterValue] = useState("");


    useEffect(() => {
        const fetchAnimals = async () => {
            let url = `/api/animals?page=${page}&pageSize=${pageSize}`;

            if (filterType === "group" && filterValue) {
                url = `/api/animals/by-group?group=${filterValue}&page=${page}&pageSize=${pageSize}`;
            } else if (filterType === "habitat" && filterValue) {
                url = `/api/animals/by-habitat?habitat=${filterValue}&page=${page}&pageSize=${pageSize}`;
            } else if (filterType === "healthStatus" && filterValue) {
                url = `/api/animals/by-health?healthStatus=${filterValue}&page=${page}&pageSize=${pageSize}`;
            } else if (filterType !== "all") {
                return;
            }
            await fetchData({
                url: url,
                onSuccess: (data) => {
                    setAnimals(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load animals data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchAnimals();
    }, [page, pageSize, navigate, filterType, filterValue]);

    const handleDelete = (id) => {
        deleteData({
            url: `/api/animals/${id}`,
            confirmMessage: "Are you sure you want to delete this animal?",
            errorMessage: "Failed to delete animal",
            onSuccess: (resData) => {
                setAnimals(animals.filter((e) => e.id !== id));
                alert(resData.message || "Animal deleted successfully");
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
            title="Animals List"
            data={animals}
            columns={[
                {name: "Name", value: (e) => e.name},
                {name: "Species", value: (e) => e.species, isWrappable: true},
                {name: "Animal Group", value: (e) => e.animalGroup},
                {name: "Habitat type", value: (e) => e.habitatType},
                { name: "Health Status", value: (e) => e.healthStatus },
                { name: "Image", value: (e) => (
                        <img
                            src={`/${e.image}`}
                            alt={e.name}
                            className="w-[190px] h-[120px] object-cover rounded-md border"
                            onError={(err) => (err.currentTarget.style.display = "none")}
                        />
                    ),},
                { name: "Last Checked At", value: (e) => e.lastCheckedUpAt.slice(0, 16) },
                { name: "Last Fed Up At", value: (e) => e.lastFedUpAt.slice(0, 16) },
            ]}
            getActions={(e) => [
                <button
                    key="delails"
                    onClick={() => navigate(`/animals/${e.id}`)}
                    className="bg-lime-600 hover:bg-lime-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                >
                    View Details
                </button>,
                <button
                    key="update"
                    onClick={() => navigate(`/animals/edit/${e.id}`)}
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
            addButtonPath="/animals/add"
            addButtonText="Add Animal"
            emptyMessage="No animals found"
            component={<AnimalFilter
                filterType={filterType}
                filterValue={filterValue}
                onFilterChange={handleFilterChange}
            />}
        />);
}
