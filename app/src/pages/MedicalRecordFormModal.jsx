import React, {useState} from "react";
import {useNavigate} from 'react-router-dom';
import {useForm} from "react-hook-form";
import {useAuth} from "../context/AuthContext.jsx";
import {submitData} from "../utils/fetch.js";
import LoadingPage from "./LoadingPage.jsx";

export default function MedicalRecordFormModal({examination, onClose}) {
    const {loading} = useAuth();
    const navigate = useNavigate();
    const [serverError, setServerError] = useState('');
    const {
        register,
        handleSubmit,
        setValue,
        watch,
        formState: {errors},
    } = useForm();

    const onSubmit = async (data) => {

        const medicalRecordPayload = {
            examinationSchedule: examination,
            diagnosis: data.diagnosis,
            treatment: data.treatment,
            notes: data.notes
        };

        setServerError('');

        await submitData({
            url: '/api/medical-records',
            method: 'POST',
            data: medicalRecordPayload,
            isJson: true,
            entityName: "Medical Record",
            navigate,
            successPath: "/my/examinations",
            onValidationError: setServerError,
        });
        onClose();

    };

    if (loading) {
        return <LoadingPage/>;
    }

    return (
        <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md relative max-h-[96vh] overflow-y-auto">
                {onClose && (
                    <button
                        onClick={onClose}
                        className="absolute top-2 right-2 text-gray-500 hover:text-red-500"
                    >
                        ✖
                    </button>
                )}
                <h2 className="text-xl font-bold mb-4">
                    Add medical record for {examination.animal.name}. <br/>Reason: {examination.reason}
                </h2>

                {serverError && (
                    <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                        {serverError}
                    </div>
                )}

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <div>
                        <label className="text-sm">Diagnosis (leave empty if everything's ok)</label>
                        <input
                            className="w-full border rounded px-3 py-2"
                            {...register("diagnosis")}
                        />
                    </div>

                    <div>
                        <label className="text-sm">Treatment</label>
                        <input
                            className="w-full border rounded px-3 py-2"
                            {...register("treatment")}
                        />
                    </div>

                    <div>
                        <label className="text-sm">Notes</label>
                        <input
                            className="w-full border rounded px-3 py-2"
                            {...register("notes")}
                        />
                    </div>

                    <div className="text-center">
                        <button
                            type="submit"
                            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                        >
                            Add record
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
