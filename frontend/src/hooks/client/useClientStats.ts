import { useQuery} from "@tanstack/react-query";
import type {ClientStatisticsDto} from "../../lib/types/ClientDtos.ts";
import * as clientApi from "../../lib/api/clientService.ts";

export function useClientStats() {
    return useQuery<ClientStatisticsDto>(
        ["clientStatistics"],
        clientApi.fetchClientStatistics()
    );
}