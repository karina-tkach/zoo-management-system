import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function CaretakerFeedingsPage() {
    const [feedings, setFeedings] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchCaretakerFeedings = async () => {
            await fetchData({
                url: `/api/feeding-schedules/caretaker?page=${page}&pageSize=${pageSize}`,
                onSuccess: (feedings) => {
                    setFeedings(feedings?.data);
                    setTotalPages(feedings?.totalPages);
                },
                errorMessage: "Failed to load caretaker feedings data",
                navigate,
                onStart: start,
                onFinally: stop,

            });
        };

        fetchCaretakerFeedings();
    }, [page, pageSize, navigate]);

    const handleEdit = async (id) => {
        if (!window.confirm('Are you sure that you want mark this animal as fed (no changes back can be made)?')) return;

        try {
            const response = await fetch(`/api/feeding-schedules/${id}/mark-done`, {
                method: "PATCH",
                credentials: "include",
            });

            const resData = await response.json();

            if (response.ok) {
                setFeedings(feedings.map(feeding =>
                    feeding.id === id
                        ? { ...feeding, doneToday: true }
                        : feeding
                ));
                alert(resData.message || "Feeding edited successfully");
            } else {
                alert(resData.message || "Cannot edit this feeding");
            }
        } catch (error) {
            navigate("/error", {
                state: {
                    message: "An unexpected error occurred while deleting",
                    code: 500,
                },
            });
        }
    };

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title="My Feedings List"
            data={feedings}
            columns={[
                { name: "Animal name", value: (e) => e.animal.name },
                { name: "Animal habitat type", value: (e) => e.animal.habitatType },
                { name: "Enclosure", value: (e) => `${e.animal.enclosure.name} | ${e.animal.enclosure.location} | ${e.animal.enclosure.areaM2}`, isWrappable: true },
                { name: "Image", value: (e) => (
                        <img
                            src={`/${e.animal.image}`}
                            alt={e.animal.name}
                            className="w-[190px] h-[120px] object-cover rounded-md border"
                            onError={(err) => (err.currentTarget.style.display = "none")}
                        />
                    ),},
                { name: "Last Fed Up At", value: (e) => e.animal.lastFedUpAt.slice(0, 16) },
                { name: "Food type", value: (e) => e.foodType },
                { name: "Feeding time", value: (e) => e.time },
                { name: "Portion size (grams)", value: (e) => e.portionSizeGrams },
            ]}
            getActions={(e) => [
                <div className="relative inline-block w-11 h-5">
                    <input id={`switch-${e.id}`} type="checkbox" checked={e.doneToday}
                           disabled={e.doneToday}
                           onChange={() => handleEdit(e.id)}
                           className="peer appearance-none w-11 h-5 bg-slate-100 rounded-full checked:bg-slate-800 cursor-pointer transition-colors duration-300"/>
                    <label htmlFor={`switch-${e.id}`}
                           className="absolute top-0 left-0 w-5 h-5 bg-white rounded-full border border-slate-300 shadow-sm transition-transform duration-300 peer-checked:translate-x-6 peer-checked:border-slate-800 cursor-pointer">
                    </label>
                </div>
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            emptyMessage="No feedings found"
        />);
}
