export const fetchData = async (
    {
        url,
        onSuccess,
        errorMessage = "Failed to load data",
        navigate,
        onStart,
        onFinally}) => {
    try {
        onStart?.();

        const response = await fetch(url, { credentials: "include" });

        if (response.status === 200) {
            const data = await response.json();
            onSuccess(data);
        } else {
            const resData = await response.json();
            navigate("/error", {
                state: {
                    message: resData?.message || errorMessage,
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
    } finally {
        onFinally?.();
    }
};

export const deleteData = async ({
                                     url,
                                     onSuccess,
                                     errorMessage = "Failed to delete item",
                                     onError,
                                     navigate,
                                     confirmMessage = "Are you sure you want to delete this item?",
                                 }) => {
    if (!window.confirm(confirmMessage)) return;

    try {
        const response = await fetch(url, {
            method: "DELETE",
            credentials: "include",
        });

        const resData = await response.json();

        if (response.ok) {
            onSuccess?.(resData);
        } else {
            onError?.(resData.message || errorMessage);
        }
    } catch (error) {
        navigate?.("/error", {
            state: {
                message: "An unexpected error occurred while deleting",
                code: 500,
            },
        });
    }
};

export const submitData = async ({
                                     url,
                                     method = "POST",
                                     data,
                                     isJson = true, // set to false for FormData
                                     entityName = "Item",
                                     navigate,
                                     successPath,
                                     onValidationError,
                                     acceptedStatus = [200, 201],
                                     extraAcceptedStatuses = [],
                                 }) => {
    try {
        const options = {
            method,
            credentials: "include",
            body: isJson ? JSON.stringify(data) : data,
            headers: isJson ? { "Content-Type": "application/json" } : undefined,
        };

        const response = await fetch(url, options);
        const status = response.status;

        if (acceptedStatus.includes(status)) {
            alert(`${entityName} ${method === "PATCH" ? "updated" : "added"} successfully`);
            navigate(successPath);
        } else if (status === 400 || extraAcceptedStatuses.includes(status)) {
            const resData = await response.json();
            onValidationError?.(resData.message || "Invalid input.");
        } else {
            const resData = await response.json();
            navigate("/error", {
                state: {
                    message: resData.message || "Something went wrong",
                    code: status,
                },
            });
        }
    } catch (error) {
        navigate("/error", {
            state: {
                message: "Something went wrong",
                code: 500,
            },
        });
    }
};

