import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Pagination from "../components/Pagination";
import ExcursionCard from "../components/ExcursionCard";

export default function ExcursionsView() {
    const [excursions, setExcursions] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(4);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading } = useAuth();


    useEffect(() => {
        const fetchExcursions = async () => {
            try {
                const response = await fetch(`/api/excursions?page=${page}&pageSize=${pageSize}`, { credentials: "include" });

                if (response.status === 200) {
                    const data = await response.json();
                    setExcursions(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                } else {
                    const resData = await response.json();
                    navigate("/error", {
                        state: {
                            message: resData.message || "Failed to load excursions data",
                            code: response.status,
                        },
                    });
                }
            } catch (error) {
                navigate("/error", {
                    state: {
                        message: "An unexpected error occurred",
                        code: 500,
                    },
                });
            }
        };

        fetchExcursions();
    }, [page, pageSize]);

    if (loading) {
        return (
            <div className="relative p-6 min-h-screen bg-gray-200">
                <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 border-solid mx-auto mb-4" />
                        <p className="text-xl font-semibold text-gray-700">Loading...</p>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-gradient-to-br from-green-50 py-8 px-8 to-green-100 scroll-target">
            <div className="min-h-[600px] py-8 px-4 grid gap-6 md:grid-cols-2 lg:grid-cols-4">
                {excursions.map((excursion) => (
                    <ExcursionCard key={excursion.id} excursion={excursion}/>
                ))}
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
};
