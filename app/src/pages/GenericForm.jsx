import {useNavigate} from "react-router-dom";
import React, {useEffect} from "react";

export function GenericForm({
                                fields,
                                isEdit,
                                entityName = "Item",
                                onSubmit,
                                cancelPath,
                                serverError,
                                register,
                                handleSubmit,
                                errors,
                                multipart = false,
                            }) {
    const navigate = useNavigate();

    useEffect(() => {
            const targetSection = document.querySelector(".scroll-target");
            if (targetSection) {
                targetSection.scrollIntoView({ behavior: "smooth" });
            }
    }, [serverError]);

    return (
        <div className="max-w-xl mx-auto mt-10 bg-white shadow-md rounded-xl p-6 scroll-target">
            <h2 className="text-2xl font-semibold mb-6 text-center">
                {isEdit ? `Update ${entityName}` : `Add ${entityName}`}
            </h2>

            {serverError && (
                <div className="mb-4 text-red-700 bg-red-100 border border-red-300 rounded p-3 text-sm">
                    {serverError}
                </div>
            )}

            <form onSubmit={handleSubmit(onSubmit)} {...(multipart ? { encType: "multipart/form-data" } : {})} className="space-y-4">
                {fields.map(field => {
                    if (field.onlyWhenAdd && isEdit) return null;

                    return (
                        <>
                        {isEdit && field.name === "image" && (<div className="mt-6 text-sm text-gray-600">
                            Leave empty to keep current image
                        </div>)}
                        <div key={field.name}>
                            <label className="block text-sm font-medium mb-1">{field.label}</label>

                            {field.type === 'select' ? (
                                <select
                                    className="w-full border border-gray-300 rounded-lg px-3 py-2"
                                    {...register(field.name, { required: field.required })}
                                >
                                    <option value="" disabled={true}>Select {field.label.toLowerCase()}</option>
                                    {field.options.map(opt => (
                                        <option key={opt.value} value={opt.value}>{opt.label}</option>
                                    ))}
                                </select>
                            ) : (
                                <input
                                    type={field.type}
                                    className="w-full border border-gray-300 rounded-lg px-3 py-2"
                                    {...register(field.name, { required: field.required })}
                                    {...(field.type === 'file' && field.accept ? { accept: field.accept } : {})}
                                />
                            )}

                            {errors[field.name] && (
                                <p className="text-red-500 text-sm mt-1">{field.label} is required</p>
                            )}
                        </div>
                        </>
                    );
                })}

                <div className="flex justify-between items-center mt-6">
                    <button type="submit" className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition">
                        {isEdit ? 'Update' : 'Add'}
                    </button>
                    <button type="button" onClick={() => navigate(cancelPath)} className="bg-gray-300 text-gray-800 px-5 py-2 rounded-lg hover:bg-gray-400 transition">
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}
