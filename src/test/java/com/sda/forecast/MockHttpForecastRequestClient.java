package com.sda.forecast;

public class MockHttpForecastRequestClient implements HttpRequestClient {
    @Override
    public String getWeatherData(Double latitude, Double longitude) {

        return "{\n" +
                "  \"lat\": %s,\n" +
                "  \"lon\": %s,\n" +
                "  \"timezone\": \"Europe/Warsaw\",\n" +
                "  \"timezone_offset\": 7200,\n" +
                "  \"daily\": [\n" +
                "    {\n" +
                "      \"dt\": 1658660400,\n" +
                "      \"sunrise\": 1658631891,\n" +
                "      \"sunset\": 1658689670,\n" +
                "      \"moonrise\": 1658617320,\n" +
                "      \"moonset\": 1658680080,\n" +
                "      \"moon_phase\": 0.87,\n" +
                "      \"temp\": {\n" +
                "        \"day\": 24.89,\n" +
                "        \"min\": 10.62,\n" +
                "        \"max\": 26.87,\n" +
                "        \"night\": 18.02,\n" +
                "        \"eve\": 26.73,\n" +
                "        \"morn\": 13.31\n" +
                "      },\n" +
                "      \"feels_like\": {\n" +
                "        \"day\": 24.27,\n" +
                "        \"night\": 17.29,\n" +
                "        \"eve\": 26.25,\n" +
                "        \"morn\": 12.81\n" +
                "      },\n" +
                "      \"pressure\": 1019,\n" +
                "      \"humidity\": 32,\n" +
                "      \"dew_point\": 7.08,\n" +
                "      \"wind_speed\": 3.98,\n" +
                "      \"wind_deg\": 288,\n" +
                "      \"wind_gust\": 6.91,\n" +
                "      \"weather\": [\n" +
                "        {\n" +
                "          \"id\": 801,\n" +
                "          \"main\": \"Clouds\",\n" +
                "          \"description\": \"few clouds\",\n" +
                "          \"icon\": \"02d\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"clouds\": 16,\n" +
                "      \"pop\": 0,\n" +
                "      \"uvi\": 6.52\n" +
                "    },\n" +
                "    {\n" +
                "      \"dt\": 1658746800,\n" +
                "      \"sunrise\": 1658718381,\n" +
                "      \"sunset\": 1658775982,\n" +
                "      \"moonrise\": 1658705520,\n" +
                "      \"moonset\": 1658770320,\n" +
                "      \"moon_phase\": 0.9,\n" +
                "      \"temp\": {\n" +
                "        \"day\": 31.68,\n" +
                "        \"min\": 16.67,\n" +
                "        \"max\": 34.72,\n" +
                "        \"night\": 18.96,\n" +
                "        \"eve\": 31.71,\n" +
                "        \"morn\": 18.92\n" +
                "      },\n" +
                "      \"feels_like\": {\n" +
                "        \"day\": 30.08,\n" +
                "        \"night\": 19.31,\n" +
                "        \"eve\": 30.6,\n" +
                "        \"morn\": 18.2\n" +
                "      },\n" +
                "      \"pressure\": 1011,\n" +
                "      \"humidity\": 26,\n" +
                "      \"dew_point\": 9.7,\n" +
                "      \"wind_speed\": 6.22,\n" +
                "      \"wind_deg\": 172,\n" +
                "      \"wind_gust\": 12.41,\n" +
                "      \"weather\": [\n" +
                "        {\n" +
                "          \"id\": 500,\n" +
                "          \"main\": \"Rain\",\n" +
                "          \"description\": \"light rain\",\n" +
                "          \"icon\": \"10d\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"clouds\": 33,\n" +
                "      \"pop\": 0.8,\n" +
                "      \"rain\": 2.19,\n" +
                "      \"uvi\": 6.86\n" +
                "    },".formatted(latitude, longitude);
    }
}
