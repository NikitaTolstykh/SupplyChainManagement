export type PaymentMethod = "CARD" | "CASH" | "BANK_TRANSACTION" | "POSTAL_PAYMENT";

export const PaymentMethodValues = {
    CARD: "CARD",
    CASH: "CASH",
    BANK_TRANSACTION: "BANK_TRANSACTION",
    POSTAL_PAYMENT: "POSTAL_PAYMENT",
} as const;