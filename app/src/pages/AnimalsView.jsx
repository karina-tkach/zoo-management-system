import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Pagination from "../components/Pagination";
import {fetchData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";
import {useLoading} from "../utils/useLoading.jsx";
import AnimalFilter from "../components/AnimalFilter.jsx";
import AnimalCard from "../components/AnimalCard.jsx";
import {Frown} from "lucide-react";

export default function AnimalsView() {
    const [animals, setAnimals] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(9);
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
        <div className="bg-gradient-to-br from-green-50 to-green-100 min-h-screen">
            <div className="max-w-7xl mx-auto p-6 scroll-target">
                <div className="text-center mb-10">
                    <p className="text-green-600 text-7xl font-bold">Zoo Animals</p>
                    <h2 className="text-4xl font-bold text-green-800 mb-5">Get to know our inhabitants</h2>
                    <AnimalFilter
                        filterType={filterType}
                        filterValue={filterValue}
                        onFilterChange={handleFilterChange}
                    />
                </div>

                {animals.length === 0 ? (
                    <div
                        className="max-w-xl mx-auto bg-red-100 shadow-md rounded-2xl p-20 mt-20 mb-40 text-center border border-gray-200">
                        <Frown className="w-20 h-20 text-red-400 mb-6 mx-auto"/>
                        <h2 className="text-3xl font-bold text-gray-700 mb-2">
                            We don't have such animals...
                        </h2>
                        <p className="text-gray-500 text-lg">
                            Try adjusting your filters or check back later!
                        </p>
                    </div>
                ) : (
                    <>
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
                            {animals.map((animal) => (
                                <AnimalCard key={animal.id} animal={animal}/>
                            ))}
                        </div>

                        <Pagination
                            currentPage={page}
                            totalPages={totalPages}
                            setCurrentPage={setPage}
                            shouldScroll={shouldScroll}
                            setShouldScroll={setShouldScroll}
                        />
                    </>
                )}
            </div>
        </div>
    );
};
