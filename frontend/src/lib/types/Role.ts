export type Role = "ADMIN" | "CLIENT" | "DISPATCHER" | "DRIVER";

export const RoleValues = {
    ADMIN: "ADMIN",
    CLIENT: "CLIENT",
    DISPATCHER: "DISPATCHER",
    DRIVER: "DRIVER",
} as const;