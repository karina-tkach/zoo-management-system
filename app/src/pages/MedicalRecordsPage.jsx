import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";
import FeedingRecordFilter from "../components/FeedingRecordFilter.jsx";

export default function MedicalRecordsPage() {
    const [medRecords, setMedRecords] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();
    const [filterType, setFilterType] = useState("all");
    const [filterValue, setFilterValue] = useState("");


    useEffect(() => {
        const fetchMedRecords = async () => {
            let url = `/api/medical-records?page=${page}&pageSize=${pageSize}`;

            if (filterType === "date" && filterValue) {
                url = `/api/medical-records/by-date?date=${filterValue}&page=${page}&pageSize=${pageSize}`;
            } else if (filterType !== "all") {
                return;
            }
            await fetchData({
                url: url,
                onSuccess: (data) => {
                    setMedRecords(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load medical records data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchMedRecords();
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
            title="Medical Records List"
            data={medRecords}
            columns={[
                { name: "Animal name", value: (e) => e.examinationSchedule.animal.name },
                { name: "Animal habitat type", value: (e) => e.examinationSchedule.animal.habitatType },
                { name: "Animal health status", value: (e) => e.examinationSchedule.animal.healthStatus },
                { name: "Enclosure", value: (e) => `${e.examinationSchedule.animal.enclosure.name} | ${e.examinationSchedule.animal.enclosure.location}`, isWrappable: true },
                { name: "Vet", value: (e) => `${e.examinationSchedule.veterinarian.name} | ${e.examinationSchedule.veterinarian.email}`, isWrappable: true },
                { name: "Planned time", value: (e) => e.examinationSchedule.plannedDateTime.slice(0, 16).replace("T", " "), isWrappable: true },
                { name: "Reason", value: (e) => e.examinationSchedule.reason },
                { name: "Diagnosis", value: (e) => e.diagnosis },
                { name: "Treatment", value: (e) => e.treatment },
                { name: "Notes", value: (e) => e.notes },
                { name: "Created at", value: (e) => e.createdAt.slice(0, 16).replace("T", " ") },
            ]}
            page={page}
            setPage={setPage}
            totalPages={totalPages}
            shouldScroll={shouldScroll}
            setShouldScroll={setShouldScroll}
            emptyMessage="No medical records found"
            component={<FeedingRecordFilter
                filterType={filterType}
                filterValue={filterValue}
                onFilterChange={handleFilterChange}
            />}
        />);
}
