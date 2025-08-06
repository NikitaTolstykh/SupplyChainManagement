import {Role} from "./Role.ts";

export interface UserResponseDto {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    phone: string;
    role: Role;
}

export interface UserRequestDto {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phone: string;
    role: Role;
}

export interface VehicleDto {
    id: number;
    brand: string;
    model: string;
    color: string;
    licensePlate: string;
    comment?: string;
    driverId: number;
}
export interface VehicleFormDto {
    brand: string;
    model: string;
    color: string;
    licensePlate: string;
    comment?: string;
    driverId: number;
}
