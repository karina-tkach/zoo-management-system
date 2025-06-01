import React, { useEffect, useState } from "react";
import {useNavigate, useParams} from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import {ArrowLeft} from "lucide-react";

export default function EnclosureAnimalsPage() {
    const { id } = useParams();
    const [animals, setAnimals] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(7);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchAnimals = async () => {
            await fetchData({
                url: `/api/animals/by-enclosure/${id}?page=${page}&pageSize=${pageSize}`,
                onSuccess: (animals) => {
                    setAnimals(animals?.data);
                    setTotalPages(animals?.totalPages);
                },
                errorMessage: "Failed to load animals data",
                navigate,
                onStart: start,
                onFinally: stop,

            });
        };

        fetchAnimals();
    }, [page, pageSize, navigate]);

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title={<>
                <button
                    onClick={() => navigate("/enclosures")}
                    className="mb-5 w-10 h-10 rounded-full bg-emerald-200 text-green-700 hover:bg-emerald-400 transition flex items-center justify-center shadow"
                    title="Back"
                >
                    <ArrowLeft className="w-5 h-5"/>
                </button>
                <span>Animals In Enclosure List</span></>}
            data={animals}
            columns={[
                {name: "Name", value: (e) => e.name},
                {name: "Species", value: (e) => e.species, isWrappable: true},
                {name: "Animal Group", value: (e) => e.animalGroup},
                {name: "Gender", value: (e) => e.gender},
                {name: "Date of birth", value: (e) => e.birthDate },
                { name: "Health Status", value: (e) => e.healthStatus },
                { name: "Image", value: (e) => (
                        <img
                            src={`/${e.image}`}
                            alt={e.name}
                            className="w-[190px] h-[120px] object-cover rounded-md border"
                            onError={(err) => (err.currentTarget.style.display = "none")}
                        />
                    ),},
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            emptyMessage="No animals found"
        />);
}
