import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import FeedingRecordFilter from "../components/FeedingRecordFilter.jsx";

export default function FeedingRecordsPage() {
    const [records, setRecords] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();
    const [filterType, setFilterType] = useState("all");
    const [filterValue, setFilterValue] = useState("");


    useEffect(() => {
        const fetchRecords = async () => {
            let url = `/api/feeding-records?page=${page}&pageSize=${pageSize}`;

            if (filterType === "date" && filterValue) {
                url = `/api/feeding-records/by-date?date=${filterValue}&page=${page}&pageSize=${pageSize}`;
            } else if (filterType !== "all") {
                return;
            }
            await fetchData({
                url: url,
                onSuccess: (data) => {
                    setRecords(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load feedings data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchRecords();
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
        <GenericTablePage
            title="Feeding Records List"
            data={records}
            columns={[
                { name: "Animal name", value: (e) => e.feedingSchedule.animal.name },
                { name: "Animal habitat type", value: (e) => e.feedingSchedule.animal.habitatType },
                { name: "Enclosure", value: (e) => `${e.feedingSchedule.animal.enclosure.name} | ${e.feedingSchedule.animal.enclosure.location} | ${e.feedingSchedule.animal.enclosure.areaM2}`, isWrappable: true },
                { name: "Caretaker", value: (e) => `${e.feedingSchedule.caretaker.name} | ${e.feedingSchedule.caretaker.email}`, isWrappable: true },
                { name: "Food type", value: (e) => e.feedingSchedule.foodType, isWrappable: true },
                { name: "Feeding time", value: (e) => e.feedingSchedule.time },
                { name: "Portion size (grams)", value: (e) => e.feedingSchedule.portionSizeGrams },
                { name: "Feeding Date", value: (e) => e.feedingDate },
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            emptyMessage="No feeding records found"
            component={<FeedingRecordFilter
                filterType={filterType}
                filterValue={filterValue}
                onFilterChange={handleFilterChange}
            />}
        />);
}
