import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";
import GenericTablePage from "./GenericTablePage.jsx";

export default function VetMedicalRecordsPage() {
    const [medRecords, setMedRecords] = useState([]);
    const [page, setPage] = useState(1);
    const [pageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(1);
    const [shouldScroll, setShouldScroll] = useState(false);
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();


    useEffect(() => {
        const fetchMedRecordsByVet = async () => {
            await fetchData({
                url: `/api/medical-records/veterinarian?page=${page}&pageSize=${pageSize}`,
                onSuccess: (data) => {
                    setMedRecords(data?.data || []);
                    setTotalPages(data?.totalPages || 1);
                },
                errorMessage: "Failed to load vet medical records data",
                navigate,
                onStart: start,
                onFinally: stop,
            });
        };

        fetchMedRecordsByVet();
    }, [page, pageSize, navigate]);


    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <GenericTablePage
            title="My Medical Records List"
            data={medRecords}
            columns={[
                { name: "Animal name", value: (e) => e.examinationSchedule.animal.name },
                { name: "Animal habitat type", value: (e) => e.examinationSchedule.animal.habitatType },
                { name: "Animal health status", value: (e) => e.examinationSchedule.animal.healthStatus },
                { name: "Enclosure", value: (e) => `${e.examinationSchedule.animal.enclosure.name} | ${e.examinationSchedule.animal.enclosure.location}`, isWrappable: true },
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
            emptyMessage="No vet medical records found"
        />);
}
